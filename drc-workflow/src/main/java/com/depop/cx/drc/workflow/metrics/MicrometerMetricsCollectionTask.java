package com.depop.cx.drc.workflow.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.impl.metrics.Meter;
import org.camunda.bpm.engine.impl.metrics.MetricsRegistry;
import org.camunda.bpm.engine.impl.metrics.reporter.MetricsCollectionTask;
import org.camunda.bpm.engine.impl.persistence.entity.MeterLogEntity;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Camunda metrics collection task
 * <p>
 * Based on {@link MetricsCollectionTask}, this task class replaces Camunda's default behaviour.
 * This tasks extends the default collection task to send metrics to micrometer.
 */
public class MicrometerMetricsCollectionTask extends MetricsCollectionTask {

    private static final Logger log = LoggerFactory.getLogger(MicrometerMetricsCollectionTask.class);
    private static final String GAUGE_PREFIX = "caumunda.engine.";

    private final MetricsRegistry camundaMetricsRegistry;
    private final MeterRegistry micrometerMetricsRegistry;
    private final Map<String, AtomicLong> gauges = new HashMap<>();


    public MicrometerMetricsCollectionTask(final MetricsRegistry camundaMetricsRegistry,
                                           final MeterRegistry micrometerMetricsRegistry,
                                           final CommandExecutor commandExecutor) {
        super(camundaMetricsRegistry, commandExecutor);
        this.camundaMetricsRegistry = camundaMetricsRegistry;
        this.micrometerMetricsRegistry = micrometerMetricsRegistry;
    }

    @Override
    protected void collectMetrics() {

        List<MeterLogEntity> logs = new ArrayList<>();
        for (Meter meter : camundaMetricsRegistry.getDbMeters().values()) {

            final String meterName = meter.getName();
            final long meterValue = meter.getAndClear();
            final Date timestamp = ClockUtil.getCurrentTime();

            log.debug("Collected metric [{}] with value [{}]", meterName, meterValue);
            getGauge(meterName).set(meterValue);

            logs.add(new MeterLogEntity(meterName, reporterId, meterValue, timestamp));

        }

        commandExecutor.execute(new MetricsCollectionCmd(logs));
    }

    private AtomicLong getGauge(final String meterName) {
        final String gaugeName = GAUGE_PREFIX + meterName.replace('-', '.');
        return gauges.computeIfAbsent(gaugeName, m -> micrometerMetricsRegistry.gauge(m, new AtomicLong(0)));
    }

}
