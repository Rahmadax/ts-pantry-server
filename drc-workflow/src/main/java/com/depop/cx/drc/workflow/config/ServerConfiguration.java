package com.depop.cx.drc.workflow.config;

import com.depop.cx.drc.workflow.metrics.engine.EngineMetricsPlugin;
import io.micrometer.core.instrument.MeterRegistry;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaMetricsConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfiguration {

    @Bean
    @ConditionalOnMissingBean(CamundaMetricsConfiguration.class)
    public CamundaMetricsConfiguration camundaMetricsConfiguration(final MeterRegistry meterRegistry) {
        return new EngineMetricsPlugin(meterRegistry);
    }

}
