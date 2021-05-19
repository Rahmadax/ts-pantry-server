package com.depop.cx.drc.workflow.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.impl.metrics.MetricsRegistry;
import org.camunda.bpm.engine.impl.metrics.reporter.DbMetricsReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Camunda metrics reporter
 * <p>
 * Based on {@link DbMetricsReporter}, this reporter class replaces Camunda's default behaviour.
 * This reporter extends the default collection task to use our custom {@link MicrometerMetricsCollectionTask}
 * to collect and report metrics to micrometer as well as the database.
 */
public class MicrometerMetricsReporter extends DbMetricsReporter {

    private static final Logger log = LoggerFactory.getLogger(MicrometerMetricsReporter.class);
    private final MetricsRegistry camundaMetricsRegistry;
    private final MeterRegistry micrometerMetricsRegistry;

    public MicrometerMetricsReporter(final MetricsRegistry camundaMetricsRegistry,
                                     final MeterRegistry micrometerMetricsRegistry,
                                     final CommandExecutor commandExecutor) {
        super(camundaMetricsRegistry, commandExecutor);
        this.camundaMetricsRegistry = camundaMetricsRegistry;
        this.micrometerMetricsRegistry = micrometerMetricsRegistry;
        initMetricsCollectionTask();
    }

    @Override
    protected void initMetricsCollectionTask() {
        super.metricsCollectionTask = new MicrometerMetricsCollectionTask(
                this.camundaMetricsRegistry,
                this.micrometerMetricsRegistry,
                super.commandExecutor);
    }

    @Override
    public void reportValueAtOnce(final String name, final long value) {
        super.reportValueAtOnce(name, value);
        log.debug("Collected metric [{}] with value [{}]", name, value);
        //TODO record the metric with micrometer here.
    }
}
