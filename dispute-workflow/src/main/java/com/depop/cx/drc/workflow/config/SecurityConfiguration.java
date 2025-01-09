package com.depop.cx.drc.workflow.config;

import com.depop.cx.drc.workflow.security.authentication.DepopJwtGrantedAuthoritiesConverter;
import com.depop.cx.drc.workflow.security.authentication.OktaOidcUserService;
import com.depop.cx.drc.workflow.security.filter.CamundaApiAuthenticationFilter;
import com.depop.cx.drc.workflow.security.filter.CamundaAppAuthenticationFilter;
import com.depop.cx.drc.workflow.security.identity.OAuth2IdentityProviderPlugin;
import com.google.common.cache.CacheBuilder;
import org.camunda.bpm.webapp.impl.security.auth.Authentications;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Spring security configuration for the dispute workflow service.
 * <p>
 * The service exposes both a rest API for other Depop services to call
 * and a web console application for internal users to access for debugging etc.
 * <p>
 * The rest API uses Depop's internal OAuth2(ish) server (client-auth) for authentication,
 * whilst the webapp uses Okta to authenticate internal users.
 *
 * @author Tom Greasley
 */
@Configuration
@EnableConfigurationProperties(ApiSecurityProperties.class)
@SuppressWarnings("Convert2MethodRef")
public class SecurityConfiguration {

    private static final String STATUS_URL = "/status";
    private static final String ERROR_URL = "/error";

    private static final String API_URL_BASE = "/internal/v1";
    private static final String API_URL_PATTERN = API_URL_BASE + "/**";

    private static final String APP_URL_BASE = "/camunda";
    private static final String APP_URL_PATTERN = APP_URL_BASE + "/**";
    private static final String APP_LOGOUT_URL = APP_URL_BASE + "/logout";
    private static final String APP_LOGOUT_REDIRECT_URL = "/camunda/app/";

    private static final String APP_AUTH_REQUEST_URL = APP_URL_BASE + OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
    private static final String APP_AUTH_RESPONSE_URL = APP_URL_BASE + OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;

    private static final String ENGINE_NAME = "default";

    private static final String ROLE_DEPOP_USER = "USER";
    private static final String ROLE_DEPOP_SUPERUSER = "SUPERUSER";
    private static final String ROLE_DRC_ADMIN = "DRC_CAMUNDA_ADMIN";

    private static final String BASIC_AUTH_REALM = "camunda-api";

    // TODO: VERSION UPGRADE: FIX THIS

    /**
     * The Web Application security is configured to:
     * - Authenticate all requests to /camunda/**
     * - Authenticate using okta.
     * - Use session based auth management.
     * - Configures CSRF to work with the camunda web application FE.
     * - Adds a custom filter to pass authentication from spring to camunda.
     */
    @Configuration
    public static class AppWebSecurityConfig {

        private final ClientRegistrationRepository clientRegistrationRepository;

        public AppWebSecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
            this.clientRegistrationRepository = clientRegistrationRepository;
        }

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
                    authentications.removeByEngineName(ENGINE_NAME);
                }
            };
        }

        @Bean
        public OAuth2IdentityProviderPlugin identityProviderPlugin() {
            return new OAuth2IdentityProviderPlugin();
        }

        @Bean
        @Order(1)
        public SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception {
            return http
                    // This chain should only match the app/console url
                    .securityMatchers((config) -> config
                            .requestMatchers(antMatcher(APP_URL_PATTERN))
                    )
                    // The oauth endpoints should be unauth'ed, but everything else requires full authentication.
                    .authorizeHttpRequests(config -> config
                            .requestMatchers(
                                    antMatcher(APP_AUTH_REQUEST_URL),
                                    antMatcher(APP_AUTH_RESPONSE_URL),
                                    antMatcher(APP_LOGOUT_URL))
                            .permitAll()
                            .anyRequest().authenticated())
                    // Use oauth2 backed by okta for authentication
                    .oauth2Login(config -> config
                            .authorizationEndpoint(endpoint -> endpoint.baseUri(APP_AUTH_REQUEST_URL))
                            .redirectionEndpoint(endpoint -> endpoint.baseUri(APP_AUTH_RESPONSE_URL))
                            .userInfoEndpoint(endpoint -> endpoint.oidcUserService(new OktaOidcUserService()))
                            .failureHandler(new SimpleUrlAuthenticationFailureHandler()))
                    .oauth2Client(withDefaults())
                    // Configure logout, integrated with camunda security
                    .logout(config -> config
                            .addLogoutHandler(camundaLogoutHandler())
                            .logoutRequestMatcher(new AntPathRequestMatcher(APP_LOGOUT_URL, HttpMethod.GET.name()))
                            .logoutSuccessHandler(oidcLogoutSuccessHandler())
                    )
                    // Disable cruft
                    .anonymous(config -> config.disable())
                    .rememberMe(config -> config.disable())
                    .csrf(config->config
                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                    // Configure camunda authentication based on the results of the oauth2 authentication
                    .addFilterAfter(new CamundaAppAuthenticationFilter(ENGINE_NAME), AuthorizationFilter.class)
                    .build();
        }
    }

    /**
     * The Default API security is configured to:
     * - Authenticate all requests to /internal/v1/**
     * - Use stateless session management (e.g. it stores no credentials on the session)
     * - Disables cors and csrf
     * - Adds a custom filter to pass authentication from spring to camunda.
     * - Uses depop JWT tokens to authenticate requests.
     */
    @Configuration
    public static class ApiWebSecurityConfig {

        private final OAuth2ResourceServerProperties.Jwt properties;

        ApiWebSecurityConfig(final OAuth2ResourceServerProperties properties) {
            this.properties = properties.getJwt();
        }

        // Define our own decoder so we can add a 10-min cache.
        protected JwtDecoder cachingJwtDecoder() {

            final var jwkSetCache = new ConcurrentMapCache("jwkSetCache", CacheBuilder.newBuilder()
                    .expireAfterWrite(Duration.ofMinutes(10))
                    .build().asMap(), false);

            final var algorithms = this.properties
                    .getJwsAlgorithms()
                    .stream()
                    .map(SignatureAlgorithm::from)
                    .collect(Collectors.toSet());

            final var nimbusJwtDecoder = NimbusJwtDecoder
                    .withJwkSetUri(this.properties.getJwkSetUri())
                    .jwsAlgorithms(set -> set.addAll(algorithms))
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
            authorityConverter.setBlockedAuthorities(List.of(ROLE_DRC_ADMIN));
            converter.setJwtGrantedAuthoritiesConverter(authorityConverter);
            return converter;
        }

        @Bean
        @Order(2)
        public SecurityFilterChain userApiFilterChain(HttpSecurity http) throws Exception {
            return http
                    // If there is a bearer token, assume this is an API call authenticated with a client-auth JWT bearer
                    .securityMatcher(request -> {
                                String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
                                return (auth != null && auth.startsWith("Bearer"));
                            }
                    )
                    // Only grant access to the following paths to depop users/superusers
                    .authorizeHttpRequests(config -> config
                            .requestMatchers(
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/process-definition/key/*"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/process-definition/*"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/process-instance/*/variables"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/process-instance/*/tasks/attachments"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/process-instance/*/variables/*"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/variable-instance"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/task"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/task/*"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/task/*/attachment"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/task/*/attachment/*"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/task/*/attachment/*/data"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/task/*/localVariables"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/history/variable-instance"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/history/process-instance/*"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/version"),
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/history/task"),
                                    antMatcher(HttpMethod.POST, API_URL_BASE + "/process-definition/key/*/start"),
                                    antMatcher(HttpMethod.POST, API_URL_BASE + "/task/*/complete"),
                                    antMatcher(HttpMethod.POST, API_URL_BASE + "/task/*/attachment/create"),
                                    antMatcher(HttpMethod.POST, API_URL_BASE + "/message"),
                                    antMatcher(HttpMethod.DELETE, API_URL_BASE + "/task/*/attachment/*")
                                    )
                            .hasAnyRole(ROLE_DEPOP_USER, ROLE_DEPOP_SUPERUSER)
                            .requestMatchers(
                                    antMatcher(HttpMethod.GET, API_URL_BASE + "/external-task/count"),
                                    antMatcher(HttpMethod.POST, API_URL_BASE + "/external-task/fetchAndLock"),
                                    antMatcher(HttpMethod.POST, API_URL_BASE + "/external-task/*/complete"),
                                    antMatcher(HttpMethod.POST, API_URL_BASE + "/external-task/*/failure"))
                            .hasAnyRole(ROLE_DEPOP_SUPERUSER)
                            .anyRequest().denyAll())
                    // Configure us as an oauth2 resource server that understands depop JWT tokens.
                    .oauth2ResourceServer(config -> config
                            .jwt(jwt -> jwt
                                    .decoder(cachingJwtDecoder())
                                    .jwtAuthenticationConverter(depopJwtAuthenticationConverter())
                            )
                    )
                    // Disable cruft.
                    .anonymous(config -> config.disable())
                    .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .cors(config -> config.disable())
                    .csrf(config -> config.disable())
                    // Configure camunda authentication based on the results of the oauth2 authentication
                    .addFilterAfter(new CamundaApiAuthenticationFilter(ENGINE_NAME), AuthorizationFilter.class)
                    .build();
        }

    }


    /**
     * The Admin API security is configured to:
     * - Authenticates all requests to /internal/v1/**
     * - Use stateless session management (e.g. it stores no credentials on the session)
     * - Disables cors and csrf
     * - Adds a custom filter to pass authentication from spring to camunda.
     * - Uses basic auth to authenticate requests.
     */
    @Configuration
    public static class AdminApiWebSecurityConfig {

        private final ApiSecurityProperties apiSecurityProperties;

        public AdminApiWebSecurityConfig(ApiSecurityProperties apiSecurityProperties) {
            this.apiSecurityProperties = apiSecurityProperties;
        }

        @Bean
        public InMemoryUserDetailsManager adminUserDetailsService() {
            UserDetails user = User.withUsername(apiSecurityProperties.getUsername())
                    .password(String.format("{noop}%s", apiSecurityProperties.getPassword()))
                    .roles(ROLE_DRC_ADMIN)
                    .build();
            return new InMemoryUserDetailsManager(user);
        }

        @Bean
        @Order(3)
        public SecurityFilterChain adminApiFilterChain(HttpSecurity http) throws Exception {
            return http
                    // This chain will handle API calls WITHOUT a bearer token (see userApiFilterChain)
                    .securityMatchers((matchers) -> matchers
                            .requestMatchers(antMatcher(API_URL_PATTERN))
                    )
                    // Authenticate every request.
                    .authorizeHttpRequests(config -> config
                            .anyRequest().hasRole(ROLE_DRC_ADMIN)
                    )
                    // Allow basic auth with a static username/password.
                    .httpBasic(config -> config
                            .realmName(BASIC_AUTH_REALM)
                    )
                    // Disable cruft
                    .anonymous(config -> config.disable())
                    .formLogin(config -> config.disable())
                    .logout(config -> config.disable())
                    .rememberMe(config -> config.disable())
                    .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .cors(config -> config.disable())
                    .csrf(config -> config.disable())
                    // Configure camunda authentication based on the results of the basic authentication
                    .addFilterAfter(new CamundaApiAuthenticationFilter(ENGINE_NAME), AuthorizationFilter.class)
                    .build();
        }
    }


    /**
     * The default security is configured to:
     * - Allow the status and error endpoints
     * - Authenticate and deny all other requests.
     */
    @Bean
    @Order(4)
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(STATUS_URL, ERROR_URL).permitAll()
                        .anyRequest().denyAll()
                ).build();
    }


    /**
     * If we are running locally with h2, bypass security for the h2 console.
     */
    @Bean
    @Profile("local & h2")
    @Order(5)
    public WebSecurityCustomizer h2WebSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toH2Console());
    }

}

