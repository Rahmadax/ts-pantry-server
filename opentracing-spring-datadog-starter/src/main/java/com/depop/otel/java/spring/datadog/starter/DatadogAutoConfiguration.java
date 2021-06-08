package com.depop.otel.java.spring.datadog.starter;

import datadog.opentracing.DDTracer;
import io.opentracing.Tracer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Datadog OpenTracing in Spring Boot
 *
 * @author Tom Greasley
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(DDTracer.class)
@ConditionalOnMissingBean(Tracer.class)
@ConditionalOnProperty(value = "opentracing.datadog.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(io.opentracing.contrib.spring.tracer.configuration.TracerAutoConfiguration.class)
@EnableConfigurationProperties(DatadogProperties.class)
public class DatadogAutoConfiguration {

    @Bean
    public Tracer tracer(final DatadogProperties properties,
                         final ObjectProvider<DatadogTracerCustomizer> customizers) {
        final var javaProperties = properties.buildProperties();
        final var tracer = DDTracer.builder().withProperties(javaProperties).build();
        customizers.orderedStream().forEach(customizer -> customizer.customize(tracer));
        datadog.trace.api.GlobalTracer.registerIfAbsent(tracer);
        return tracer;
    }

}
