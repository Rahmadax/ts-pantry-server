package com.depop.cx.drc.workflow.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.util.EngineUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * A abstract {@link OncePerRequestFilter} that bridges Spring security to Camunda security.
 * <p>
 * Concrete classes must take appropriate action to convert Spring security authentication information
 * into Camunda engine authentication.
 * <p>
 * Note:  Does not support multi-engine installations.
 *
 * @author Tom Greasley
 */
@SuppressWarnings("NullableProblems")
public abstract class CamundaAuthenticationFilter extends OncePerRequestFilter {

    protected static final String OPSTOOLS_CLAIM = "opstools";
    private static final String ROLE_PREFIX = "ROLE_";
    private final String engineName;

    protected CamundaAuthenticationFilter(final String engineName) {
        this.engineName = engineName;
    }

    protected static List<String> getUserGroups(final Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(res -> res.startsWith(ROLE_PREFIX))
                .map(res -> res.replaceFirst("^" + ROLE_PREFIX, ""))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    @Override
    public void doFilterInternal(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final FilterChain chain) throws IOException, ServletException {

        final var engine = EngineUtil.lookupProcessEngine(this.engineName);
        if (engine == null) {
            response.sendError(404, "Process engine not available");
            return;
        }
        doFilterWithEngine(request, response, chain, engine);

    }

    protected boolean isOpstool(final Authentication authentication) {
        final var principal = authentication.getPrincipal();
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            return jwt.hasClaim(OPSTOOLS_CLAIM) && (boolean) jwt.getClaim(OPSTOOLS_CLAIM);
        }
        return false;
    }

    protected abstract void doFilterWithEngine(final HttpServletRequest req,
                                               final HttpServletResponse resp,
                                               final FilterChain chain,
                                               final ProcessEngine engine) throws ServletException, IOException;

}
