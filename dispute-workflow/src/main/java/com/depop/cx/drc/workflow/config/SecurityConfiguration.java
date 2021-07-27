package com.depop.cx.drc.workflow.config;

import com.depop.cx.drc.workflow.security.authentication.DepopJwtGrantedAuthoritiesConverter;
import com.depop.cx.drc.workflow.security.filter.CamundaApiAuthenicationFilter;
import com.depop.cx.drc.workflow.security.filter.CamundaAppAuthenticationFilter;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

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
    private static final String APP_URL_PATTERN = "/camunda/**";
    private static final String ENGINE_NAME = "default";

    /**
     * The API security is configured to:
     * - Authenticate all requests to /rest/**
     * - Use stateless session management (e.g. it stores no credentials on the session)
     * - Disables cors and csrf
     * - Adds a custom filter to pass authentication from spring to camunda.
     * - Uses depop JWT tokens to authenticate requests.
     */
    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final OAuth2ResourceServerProperties.Jwt properties;

        ApiWebSecurityConfig(final OAuth2ResourceServerProperties properties) {
            this.properties = properties.getJwt();
        }

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http.antMatcher(API_URL_PATTERN)
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .csrf()
                    .disable()
                    .cors()
                    .disable()
                    .addFilterAfter(new CamundaApiAuthenicationFilter(ENGINE_NAME), AnonymousAuthenticationFilter.class)
                    .oauth2ResourceServer()
                    .jwt()
                    .decoder(cachingJwtDecoder())
                    .jwtAuthenticationConverter(depopJwtAuthenticationConverter());
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
     * The Web Application security is configured to:
     * - Authenticate all requests to /camunda/**
     * - Use session based auth management.
     * - Configures CSRF to work with the camunda web application FE.
     * - Adds a custom filter to pass authentication from spring to camunda.
     * - Uses a basic in-memory user DB NOTE: THIS SHOULD BE CHANGED TO USE OKTA.
     */
    @Configuration
    @Order(2)
    public static class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Bean
        @Override
        @SuppressWarnings("deprecation")
        public UserDetailsService userDetailsService() {
            final var manager = new InMemoryUserDetailsManager();
            manager.createUser(User.withDefaultPasswordEncoder().username("demo").password("demo").roles("ACTUATOR", "camunda-admin").build());
            manager.createUser(User.withDefaultPasswordEncoder().username("john").password("john").roles("camunda-user").build());
            return manager;
        }

        @Override
        protected void configure(final HttpSecurity http) throws Exception {

            http.antMatcher(APP_URL_PATTERN)
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic()
                    .and()
                    .rememberMe()
                    .disable()
                    .addFilterAfter(new CamundaAppAuthenticationFilter(ENGINE_NAME), AnonymousAuthenticationFilter.class)
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }

    }


    /**
     * The default security is configured to:
     * - Authenticate deny all requests.
     */
    @Configuration
    @Order(3)
    public static class DefaultWebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http.logout().disable()
                    .authorizeRequests().anyRequest().denyAll();
        }
    }
}
