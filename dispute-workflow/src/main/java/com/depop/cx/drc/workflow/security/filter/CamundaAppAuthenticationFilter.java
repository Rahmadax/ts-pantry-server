package com.depop.cx.drc.workflow.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.camunda.bpm.webapp.impl.security.auth.Authentication;
import org.camunda.bpm.webapp.impl.security.auth.AuthenticationUtil;
import org.camunda.bpm.webapp.impl.security.auth.Authentications;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

    public static final String[] APPS = new String[]{"welcome","cockpit", "tasklist", "admin"};

    public CamundaAppAuthenticationFilter(final String engineName) {
        super(engineName);
    }

    public void doFilterWithEngine(final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   final FilterChain chain,
                                   final ProcessEngine engine) throws ServletException, IOException {

        final var oidcAuthentication = extractAuthenticatedUser();
        if (oidcAuthentication.isAuthenticated()) {
            HttpSession session = request.getSession(true);
            final var camundaAuthentications = AuthenticationUtil.getAuthsFromSession(session);
            final var oidcAuthenticatedUser = oidcAuthentication.getAuthenticatedUser();
            if (!existsAuthentication(camundaAuthentications, engine, oidcAuthenticatedUser)) {
                final var groups = oidcAuthentication.getGroups();
                final var tenants = oidcAuthentication.getTenants();
                final var camunda_authentication =
                        AuthenticationUtil.createAuthentication(engine, oidcAuthenticatedUser, groups, tenants);
                camundaAuthentications.addOrReplace(camunda_authentication);
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

}
