package com.depop.cx.drc.workflow.security.filter;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.util.EngineUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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

    private final String engineName;

    protected CamundaAuthenticationFilter(final String engineName) {
        this.engineName = engineName;
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

    protected abstract void doFilterWithEngine(final HttpServletRequest req,
                                               final HttpServletResponse resp,
                                               final FilterChain chain,
                                               final ProcessEngine engine) throws ServletException, IOException;

}
