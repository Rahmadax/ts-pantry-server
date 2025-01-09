package com.depop.cx.drc.workflow.config;

import com.depop.cx.drc.workflow.metrics.engine.EngineCustomMetricsPlugin;
import com.depop.cx.drc.workflow.metrics.engine.EngineMetricsPlugin;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaMetricsConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class MetricsConfiguration {

    @Value("${METRICS_PREFIX:depop.service.stdv1.custom.counter.workflow.}")
    private String prefix;

    @Bean
    MeterBinder jvmMetrics() {
        return registry -> {
            new ClassLoaderMetrics().bindTo(registry);
            new JvmMemoryMetrics().bindTo(registry);
            new JvmGcMetrics().bindTo(registry);
            new ProcessorMetrics().bindTo(registry);
            new JvmThreadMetrics().bindTo(registry);
        };
    }

    @Bean
    @ConditionalOnMissingBean(CamundaMetricsConfiguration.class)
    public CamundaMetricsConfiguration camundaMetricsConfiguration(final MeterRegistry meterRegistry) {
        return new EngineMetricsPlugin(meterRegistry, prefix);
    }

    @Bean
    @ConditionalOnMissingBean(EngineCustomMetricsPlugin.class)
    public EngineCustomMetricsPlugin camundaCustomMetricsConfiguration(final MeterRegistry meterRegistry,
                                                                       final ProcessEngine processEngine) {
        return new EngineCustomMetricsPlugin(meterRegistry, processEngine, prefix);
    }

}
