package com.depop.cx.drc.workflow.config;

import datadog.opentracing.DDTracer;
import io.opentracing.Tracer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Datadog OpenTracing in Spring Boot
 *
 * @author Tom Greasley
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(DDTracer.class)
@ConditionalOnMissingBean(Tracer.class)
@ConditionalOnProperty(value = "opentracing.datadog.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(io.opentracing.contrib.spring.tracer.configuration.TracerAutoConfiguration.class)
@EnableConfigurationProperties(TracerProperties.class)
public class TracingConfiguration {

    @Bean
    public Tracer tracer(final TracerProperties properties) {
        final var javaProperties = properties.buildProperties();
        final var tracer = DDTracer.builder().withProperties(javaProperties).build();
        datadog.trace.api.GlobalTracer.registerIfAbsent(tracer);
        return tracer;
    }

}
