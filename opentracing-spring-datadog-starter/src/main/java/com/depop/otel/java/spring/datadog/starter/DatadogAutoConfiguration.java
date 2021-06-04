package com.depop.otel.java.spring.datadog.starter;

import datadog.opentracing.DDTracer;
import io.opentracing.Tracer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConditionalOnClass(DDTracer.class)
@ConditionalOnMissingBean(Tracer.class)
@ConditionalOnProperty(value = "opentracing.datadog.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(io.opentracing.contrib.spring.tracer.configuration.TracerAutoConfiguration.class)
@EnableConfigurationProperties(DatadogProperties.class)
public class DatadogAutoConfiguration {

    @Bean
    public Tracer tracer(final DatadogProperties properties) {
        final Properties javaProperties = properties.getJavaProperties();
        final DDTracer tracer = DDTracer.builder().withProperties(javaProperties).build();
        datadog.trace.api.GlobalTracer.registerIfAbsent(tracer);
        return tracer;
    }

}
