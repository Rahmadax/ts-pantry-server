package com.depop.cx.drc.workflow.config.camunda;

import com.depop.cx.drc.workflow.metrics.MicrometerMetricsReporter;
import io.micrometer.core.instrument.MeterRegistry;
import org.camunda.bpm.engine.impl.metrics.reporter.DbMetricsReporter;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaMetricsConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultMetricsConfiguration;
import org.camunda.bpm.spring.boot.starter.property.MetricsProperty;

import javax.annotation.PostConstruct;

/**
 * Camunda metrics configuration
 * <p>
 * Based on {@link DefaultMetricsConfiguration}, this configuration class replaces Camunda's own default
 * {@link DbMetricsReporter} with our own {@link MicrometerMetricsReporter}.  This reporter wraps the default database
 * reporter and additionally sends metrics to micrometer.
 *
 * @see <a href="https://docs.camunda.org/manual/7.15/user-guide/process-engine/metrics/#metrics-reporter">Metrics Configuration</a>
 */
public class DrcMetricsConfiguration extends AbstractCamundaConfiguration implements CamundaMetricsConfiguration {

    private final MeterRegistry micrometerRegistry;
    private MetricsProperty metrics;

    public DrcMetricsConfiguration(final MeterRegistry micrometerRegistry) {
        this.micrometerRegistry = micrometerRegistry;
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
        final var camundaRegistry = configuration.getMetricsRegistry();
        final var executor = configuration.getCommandExecutorTxRequired();
        final var reporter = new MicrometerMetricsReporter(camundaRegistry, micrometerRegistry, executor);
        reporter.setReportingIntervalInSeconds(30);
        configuration.setDbMetricsReporter(reporter);

    }
}
