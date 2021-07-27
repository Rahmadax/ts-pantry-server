package com.depop.cx.drc.workflow.security.filter;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.camunda.bpm.webapp.impl.security.auth.Authentication;
import org.camunda.bpm.webapp.impl.security.auth.AuthenticationService;
import org.camunda.bpm.webapp.impl.security.auth.Authentications;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A basic, stateless, {@link OncePerRequestFilter} that bridges Spring security to Camunda security for the
 * Camunda web application.
 * <p>
 * On each request, the filter translates the spring security authentication information into a matching
 * configuration on the camunda web application session.
 * <p>
 * Note:  Does not support multi-engine installations.
 *
 * @author Tom Greasley
 */
public class CamundaAppAuthenticationFilter extends CamundaAuthenticationFilter {

    protected AuthenticationService userAuthentications = new AuthenticationService();

    public CamundaAppAuthenticationFilter(final String engineName) {
        super(engineName);
    }

    protected static List<String> getUserGroups(final org.springframework.security.core.Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(res -> res.replaceFirst("^ROLE_", ""))
                .collect(Collectors.toList());
    }

    public void doFilterWithEngine(final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   final FilterChain chain,
                                   final ProcessEngine engine) throws ServletException, IOException {

        final var authenticationResult = extractAuthenticatedUser();
        if (authenticationResult.isAuthenticated()) {

            final var authentications = Authentications.getFromSession(request.getSession());
            final var authenticatedUser = authenticationResult.getAuthenticatedUser();

            if (!existsAuthentication(authentications, engine, authenticatedUser)) {
                final var groups = authenticationResult.getGroups();
                final var tenants = authenticationResult.getTenants();
                final var authentication = createAuthentication(engine, authenticatedUser, groups, tenants);
                authentications.addAuthentication(authentication);
            }

            chain.doFilter(request, response);
        } else {
            response.setStatus(Status.UNAUTHORIZED.getStatusCode());
        }

    }

    protected AuthenticationResult extractAuthenticatedUser() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        var authenticationResult = AuthenticationResult.unsuccessful();
        if (authentication != null && StringUtils.isNotEmpty(authentication.getName())) {
            authenticationResult = new AuthenticationResult(authentication.getName(), true);
            authenticationResult.setGroups(getUserGroups(authentication));
        }
        return authenticationResult;
    }

    protected boolean existsAuthentication(final Authentications authentications,
                                           final ProcessEngine engine,
                                           final String username) {
        final var authentication = authentications.getAuthenticationForProcessEngine(engine.getName());
        return authentication != null && isAuthenticated(authentication, engine, username);
    }

    protected boolean isAuthenticated(final Authentication authentication,
                                      final ProcessEngine engine,
                                      final String username) {
        final var processEngineName = authentication.getProcessEngineName();
        final var identityId = authentication.getIdentityId();
        return processEngineName.equals(engine.getName()) && identityId.equals(username);
    }

    protected Authentication createAuthentication(final ProcessEngine processEngine,
                                                  final String username,
                                                  final List<String> groups,
                                                  final List<String> tenants) {
        return userAuthentications.createAuthenticate(processEngine, username, groups, tenants);
    }

}
