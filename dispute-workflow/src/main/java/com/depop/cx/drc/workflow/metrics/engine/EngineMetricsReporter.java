package com.depop.cx.drc.workflow.metrics.engine;

import io.micrometer.core.instrument.MeterRegistry;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.impl.metrics.MetricsRegistry;
import org.camunda.bpm.engine.impl.metrics.reporter.DbMetricsReporter;

/**
 * Camunda metrics reporter
 * <p>
 * Based on {@link DbMetricsReporter}, this reporter class replaces Camunda's default behaviour.
 * This reporter extends the default reporting by adding a custom collector task {@link EngineMetricsCollectionTask}
 * to collect and report metrics to micrometer as well as the database.
 */
public class EngineMetricsReporter extends DbMetricsReporter {

    private final MetricsRegistry camundaMetricsRegistry;
    private final EngineMetricsRecorder micrometerRecorder;

    public EngineMetricsReporter(final MetricsRegistry camundaMetricsRegistry,
                                 final MeterRegistry micrometerMetricsRegistry,
                                 final CommandExecutor commandExecutor) {
        super(camundaMetricsRegistry, commandExecutor);
        this.camundaMetricsRegistry = camundaMetricsRegistry;
        this.micrometerRecorder = new EngineMetricsRecorder(micrometerMetricsRegistry);
        initMetricsCollectionTask();
    }

    @Override
    protected void initMetricsCollectionTask() {
        super.metricsCollectionTask = new EngineMetricsCollectionTask(
                this.camundaMetricsRegistry,
                this.micrometerRecorder,
                super.commandExecutor);
    }

    @Override
    public void reportValueAtOnce(final String name, final long value) {
        super.reportValueAtOnce(name, value);
        micrometerRecorder.recordMetric(name, value);
    }

}
