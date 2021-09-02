package com.depop.cx.drc.workflow.metrics.engine;

import io.micrometer.core.instrument.MeterRegistry;
import org.camunda.bpm.engine.impl.metrics.reporter.DbMetricsReporter;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaMetricsConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultMetricsConfiguration;
import org.camunda.bpm.spring.boot.starter.property.MetricsProperty;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Camunda metrics plugin
 * <p>
 * Based on {@link DefaultMetricsConfiguration}, this configuration class replaces Camunda's own default
 * {@link DbMetricsReporter} with our own {@link EngineMetricsReporter}.  This reporter wraps the default database
 * reporter and additionally sends metrics to micrometer.
 *
 * @see <a href="https://docs.camunda.org/manual/7.15/user-guide/process-engine/metrics/#metrics-reporter">Metrics Configuration</a>
 */
public class EngineMetricsPlugin extends AbstractCamundaConfiguration implements CamundaMetricsConfiguration {

    private final MeterRegistry micrometerRegistry;
    private final String prefix;
    private MetricsProperty metrics;

    public EngineMetricsPlugin(final MeterRegistry micrometerRegistry, final String prefix) {
        this.micrometerRegistry = micrometerRegistry;
        this.prefix = prefix;
    }

    @PostConstruct
    void init() {
        metrics = camundaBpmProperties.getMetrics();
    }

    @Override
    public void preInit(final SpringProcessEngineConfiguration configuration) {
        configuration.setMetricsEnabled(metrics.isEnabled());
        configuration.setDbMetricsReporterActivate(metrics.isDbReporterActivate());
    }

    @Override
    public void postInit(final SpringProcessEngineConfiguration configuration) {

        LoggerFactory.getLogger(EngineMetricsPlugin.class).info("Registering custom engine metrics reporter.");

        final var camundaRegistry = configuration.getMetricsRegistry();
        final var executor = configuration.getCommandExecutorTxRequired();
        final var reporter = new EngineMetricsReporter(camundaRegistry, micrometerRegistry, executor, prefix);
        reporter.setReportingIntervalInSeconds(30);
        configuration.setDbMetricsReporter(reporter);

    }
}
