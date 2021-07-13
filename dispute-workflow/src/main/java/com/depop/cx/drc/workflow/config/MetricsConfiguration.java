package com.depop.cx.drc.workflow.config;

import com.depop.cx.drc.workflow.metrics.engine.EngineCustomMetricsPlugin;
import com.depop.cx.drc.workflow.metrics.engine.EngineMetricsPlugin;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaMetricsConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class MetricsConfiguration {

    @Bean
    @ConditionalOnMissingBean(CamundaMetricsConfiguration.class)
    public CamundaMetricsConfiguration camundaMetricsConfiguration(final MeterRegistry meterRegistry) {
//        meterRegistry.config().commonTags("host_tag", "dispute-workflow");

        new ClassLoaderMetrics().bindTo(meterRegistry);
        new JvmMemoryMetrics().bindTo(meterRegistry);
        new JvmGcMetrics().bindTo(meterRegistry);
        new ProcessorMetrics().bindTo(meterRegistry);
        new JvmThreadMetrics().bindTo(meterRegistry);

        return new EngineMetricsPlugin(meterRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(EngineCustomMetricsPlugin.class)
    public EngineCustomMetricsPlugin camundaCustomMetricsConfiguration(final MeterRegistry meterRegistry, final ProcessEngine processEngine) {
        return new EngineCustomMetricsPlugin(meterRegistry, processEngine);
    }

}
