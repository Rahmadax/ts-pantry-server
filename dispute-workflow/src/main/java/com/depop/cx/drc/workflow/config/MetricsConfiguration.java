package com.depop.cx.drc.workflow.config;

import com.depop.cx.drc.workflow.metrics.engine.EngineCustomMetricsPlugin;
import com.depop.cx.drc.workflow.metrics.engine.EngineJavaMetricsPlugin;
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
        meterRegistry.config().commonTags("service_domain", "api");
        meterRegistry.config().commonTags("service_group", "bpmn");
        meterRegistry.config().commonTags("service_name", "dispute-workflow");
        meterRegistry.config().commonTags("service_role", "bpmn");

        //ex:client/controller/service/dao/listener
        meterRegistry.config().commonTags("app.layer", "service");
        meterRegistry.config().commonTags("class.function", "MetricsConfiguration.camundaMetricsConfiguration");

        return new EngineMetricsPlugin(meterRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(EngineCustomMetricsPlugin.class)
    public EngineCustomMetricsPlugin camundaCustomMetricsConfiguration(final MeterRegistry meterRegistry, final ProcessEngine processEngine) {
        meterRegistry.config().commonTags("service_domain", "api");
        meterRegistry.config().commonTags("service_group", "bpmn");
        meterRegistry.config().commonTags("service_name", "dispute-workflow");
        meterRegistry.config().commonTags("service_role", "bpmn");

        //ex:client/controller/service/dao/listener
        meterRegistry.config().commonTags("app.layer", "service");
        meterRegistry.config().commonTags("class.function", "MetricsConfiguration.camundaCustomMetricsConfiguration");

        return new EngineCustomMetricsPlugin(meterRegistry, processEngine);
    }

    @Bean
    @ConditionalOnMissingBean(EngineJavaMetricsPlugin.class)
    public EngineJavaMetricsPlugin javaMetricsConfiguration(final MeterRegistry meterRegistry, final ProcessEngine processEngine) {
        meterRegistry.config().commonTags("service_domain", "api");
        meterRegistry.config().commonTags("service_group", "bpmn");
        meterRegistry.config().commonTags("service_name", "dispute-workflow");
        meterRegistry.config().commonTags("service_role", "bpmn");

        //ex:client/controller/service/dao/listener
        meterRegistry.config().commonTags("app.layer", "service");
        meterRegistry.config().commonTags("class.function", "MetricsConfiguration.javaMetricsConfiguration");

        return new EngineJavaMetricsPlugin(meterRegistry, processEngine);
    }

}
