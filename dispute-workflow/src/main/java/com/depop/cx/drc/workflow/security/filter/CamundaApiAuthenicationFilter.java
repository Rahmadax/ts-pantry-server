package com.depop.cx.drc.workflow.security.filter;


import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A basic, stateless, {@link OncePerRequestFilter} that bridges Spring security to Camunda security for the
 * Camunda rest API.
 * <p>
 * On each request, the filter translates the spring security authentication information into a matching
 * configuration on the camunda engine.
 * <p>
 * Note:  Does not support multi-engine installations.
 *
 * @author Tom Greasley
 */

public class CamundaApiAuthenicationFilter extends CamundaAuthenticationFilter {

    public CamundaApiAuthenicationFilter(final String engineName) {
        super(engineName);
    }

    @Override
    public void doFilterWithEngine(final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   final FilterChain chain,
                                   final ProcessEngine engine) throws ServletException, IOException {

        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        final var principal = authentication.getPrincipal();

        if (principal instanceof Jwt) {
            final var username = ((Jwt) principal).getSubject();
            engine.getIdentityService().setAuthentication(username, getUserGroups(authentication));
        } else {
            engine.getIdentityService().clearAuthentication();
        }

        try {
            chain.doFilter(request, response);
        } finally {
            engine.getIdentityService().clearAuthentication();
        }
    }

}
