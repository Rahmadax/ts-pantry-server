package com.depop.otel.java.spring.datadog.starter;

import datadog.opentracing.DDTracer;

/**
 * Callback interface for customizing {@code DDTracer} beans.
 *
 * @author Tom Greasley
 * @since 1.0.0
 */
@FunctionalInterface
public interface DatadogTracerCustomizer {

    /**
     * Customize the default {@link DDTracer}.
     *
     * @param tracer the tracer to customize
     */
    void customize(DDTracer tracer);

}
