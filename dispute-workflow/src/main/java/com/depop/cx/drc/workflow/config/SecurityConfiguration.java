package com.depop.cx.drc.workflow.config;

import com.depop.cx.drc.workflow.security.authentication.DepopJwtGrantedAuthoritiesConverter;
import com.depop.cx.drc.workflow.security.authentication.OktaOidcUserService;
import com.depop.cx.drc.workflow.security.filter.CamundaApiAuthenicationFilter;
import com.depop.cx.drc.workflow.security.filter.CamundaAppAuthenticationFilter;
import com.google.common.cache.CacheBuilder;
import org.camunda.bpm.webapp.impl.security.auth.Authentications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.time.Duration;

/**
 * Spring security configuration for the dispute workflow service.
 * <p>
 * The serive exposes both a rest API for other depop services to call
 * and a web application for internal uses to access for debugging etc.
 * <p>
 * The rest API uses depop's internal OAuth2 server for authentication,
 * whilst the webapp uses Okta to authenticate internal users.
 *
 * @author Tom Greasley
 */
@Configuration
@SuppressWarnings("java:S1118")
public class SecurityConfiguration {

    private static final String API_URL_PATTERN = "/internal/v1/**";

    private static final String APP_URL_BASE = "/camunda";
    private static final String APP_URL_PATTERN = APP_URL_BASE + "/**";
    private static final String APP_LOGOUT_URL = APP_URL_BASE + "/logout";
    private static final String APP_LOGOUT_REDIRECT_URL = "/camunda/app/";

    private static final String APP_AUTH_REQUEST_URL = APP_URL_BASE + OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
    private static final String APP_AUTH_RESPONSE_URL = APP_URL_BASE + OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;

    private static final String STATUS_URL = "/status";
    private static final String ENGINE_NAME = "default";


    /**
     * The Web Application security is configured to:
     * - Authenticate all requests to /camunda/**
     * - Authenticate using okta.
     * - Use session based auth management.
     * - Configures CSRF to work with the camunda web application FE.
     * - Adds a custom filter to pass authentication from spring to camunda.
     */
    @Configuration
    @Order(1)
    public static class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private ClientRegistrationRepository clientRegistrationRepository;

        protected OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
            OidcClientInitiatedLogoutSuccessHandler successHandler
                    = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
            successHandler.setDefaultTargetUrl(APP_LOGOUT_REDIRECT_URL);
            successHandler.setAlwaysUseDefaultTargetUrl(true);
            return successHandler;
        }

        protected LogoutHandler camundaLogoutHandler() {
            return (request, response, authentication) -> {
                final Authentications authentications = Authentications.getCurrent();
                if (authentications != null) {
                    authentications.removeAuthenticationForProcessEngine(ENGINE_NAME);
                }
            };
        }

        @Override
        protected void configure(final HttpSecurity http) throws Exception {

            http.antMatcher(APP_URL_PATTERN)

                    // Allow all oauth2 and logout requests, authenticate everything else.
                    .authorizeRequests().antMatchers(APP_AUTH_REQUEST_URL, APP_AUTH_RESPONSE_URL, APP_LOGOUT_URL).permitAll()
                    .anyRequest().authenticated()
                    .and()

                    // Configure oauth2 login, changing the default base paths of the auth and redirection endpoints to
                    // include the web app base /camunda/
                    .oauth2Login()
                    .authorizationEndpoint()
                    .baseUri(APP_AUTH_REQUEST_URL).and()
                    .redirectionEndpoint()
                    .baseUri(APP_AUTH_RESPONSE_URL).and()
                    .userInfoEndpoint().oidcUserService(new OktaOidcUserService())
                    .and()
                    .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                    .and()

                    .oauth2Client()
                    .and()

                    // Configure a logout handler
                    .logout()
                    .addLogoutHandler(camundaLogoutHandler())
                    .logoutRequestMatcher(new AntPathRequestMatcher(APP_LOGOUT_URL, HttpMethod.GET.name()))
                    .logoutSuccessHandler(oidcLogoutSuccessHandler())
                    .and()

                    // Configure cors and remember me etc.
                    .rememberMe().disable()
                    .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .and()

                    // Add a filter to copy spring security authentication over to the camunda session.
                    .addFilterAfter(new CamundaAppAuthenticationFilter(ENGINE_NAME), AnonymousAuthenticationFilter.class);
        }

    }

    /**
     * The API security is configured to:
     * - Authenticate all requests to /internal/v1/**
     * - Use stateless session management (e.g. it stores no credentials on the session)
     * - Disables cors and csrf
     * - Adds a custom filter to pass authentication from spring to camunda.
     * - Uses depop JWT tokens to authenticate requests.
     */
    @Configuration
    @Order(2)
    public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final OAuth2ResourceServerProperties.Jwt properties;

        ApiWebSecurityConfig(final OAuth2ResourceServerProperties properties) {
            this.properties = properties.getJwt();
        }

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http.antMatcher(API_URL_PATTERN)

                    // Authenticate all requests.
                    .authorizeRequests().anyRequest().authenticated()
                    .and()

                    // Enable JWT based authentication
                    .oauth2ResourceServer().jwt().decoder(cachingJwtDecoder()).jwtAuthenticationConverter(depopJwtAuthenticationConverter()).and()
                    .and()

                    // Disable all state management.
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                    // Disable cors etc.
                    .and()
                    .csrf().disable()
                    .cors().disable()

                    // Add a filter to copy spring security authentication over to camunda.
                    .addFilterAfter(new CamundaApiAuthenicationFilter(ENGINE_NAME), AnonymousAuthenticationFilter.class);

        }

        // Define our own decoder so we can add a 10 min cache.
        protected JwtDecoder cachingJwtDecoder() {

            final var jwkSetCache = new ConcurrentMapCache("jwkSetCache", CacheBuilder.newBuilder()
                    .expireAfterWrite(Duration.ofMinutes(10))
                    .build().asMap(), false);

            final var nimbusJwtDecoder = NimbusJwtDecoder
                    .withJwkSetUri(this.properties.getJwkSetUri())
                    .jwsAlgorithm(SignatureAlgorithm.from(this.properties.getJwsAlgorithm()))
                    .cache(jwkSetCache)
                    .build();

            final var issuerUri = this.properties.getIssuerUri();
            if (issuerUri != null) {
                nimbusJwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuerUri));
            }

            return nimbusJwtDecoder;
        }

        // Define our own authentication converter so we can deal with the custom depop claims/grants.
        protected JwtAuthenticationConverter depopJwtAuthenticationConverter() {
            final var converter = new JwtAuthenticationConverter();
            final var authorityConverter = new DepopJwtGrantedAuthoritiesConverter();
            converter.setJwtGrantedAuthoritiesConverter(authorityConverter);
            return converter;
        }

    }

    /**
     * The /status security is configured to:
     * - Authenticate and allow all requests.
     */
    @Configuration
    @Order(3)
    public static class StatusWebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {

            // Allow all requests to /status
            http.mvcMatcher(STATUS_URL)
                    .authorizeRequests()
                    .anyRequest().permitAll();
        }
    }

    /**
     * The default security is configured to:
     * - Authenticate and deny all requests.
     */
    @Configuration
    @Order(4)
    public static class DefaultWebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {

            // Deny all requests by default.
            http.authorizeRequests()
                    .antMatchers("/error").permitAll()
                    .anyRequest().denyAll();
        }
    }

    /**
     * If we are running locally with h2, bypass security for the h2 console.
     */
    @Configuration
    @Profile("local & h2")
    @Order(5)
    public static class H2ConsoleWebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(WebSecurity web) {
            web.ignoring().requestMatchers(PathRequest.toH2Console());
        }
    }

}
