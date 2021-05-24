package com.depop.cx.drc.workflow.metrics.engine;

import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.impl.metrics.Meter;
import org.camunda.bpm.engine.impl.metrics.MetricsRegistry;
import org.camunda.bpm.engine.impl.metrics.reporter.MetricsCollectionTask;
import org.camunda.bpm.engine.impl.persistence.entity.MeterLogEntity;
import org.camunda.bpm.engine.impl.util.ClockUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Camunda metrics collection task
 * <p>
 * Based on {@link MetricsCollectionTask}, this task class replaces Camunda's default behaviour.
 * This tasks extends the default collection task to additionally send metrics to micrometer.
 */
public class EngineMetricsCollectionTask extends MetricsCollectionTask {

    private final MetricsRegistry camundaMetricsRegistry;
    private final EngineMetricsRecorder micrometerRecorder;

    public EngineMetricsCollectionTask(final MetricsRegistry camundaMetricsRegistry,
                                       final EngineMetricsRecorder micrometerRecorder,
                                       final CommandExecutor commandExecutor) {
        super(camundaMetricsRegistry, commandExecutor);
        this.camundaMetricsRegistry = camundaMetricsRegistry;
        this.micrometerRecorder = micrometerRecorder;
    }

    @Override
    protected void collectMetrics() {

        List<MeterLogEntity> logs = new ArrayList<>();
        for (Meter meter : camundaMetricsRegistry.getDbMeters().values()) {

            final String meterName = meter.getName();
            final long meterValue = meter.getAndClear();
            final Date timestamp = ClockUtil.getCurrentTime();

            // Record in micrometer
            micrometerRecorder.recordMetric(meterName, meterValue);

            // Record in the database
            logs.add(new MeterLogEntity(meterName, reporterId, meterValue, timestamp));
        }

        commandExecutor.execute(new MetricsCollectionCmd(logs));
    }

}
