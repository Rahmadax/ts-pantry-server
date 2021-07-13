package com.depop.cx.drc.workflow.metrics.engine;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class EngineMetricsRecorder {

    private static final Logger log = LoggerFactory.getLogger(EngineMetricsRecorder.class);
    private static final String GAUGE_PREFIX = "depop.service.dispute_workflow.camunda.engine.";

    private final MeterRegistry registry;
    private final Map<String, AtomicLong> gauges = new ConcurrentHashMap<>();

    public EngineMetricsRecorder(final MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordMetric(final String name, final long value) {
        final String gaugeName = GAUGE_PREFIX + name.replace('-', '.');
        log.debug("Recorded metric [{}] with value [{}]", gaugeName, value);
        getGauge(gaugeName).set(value);
    }

    protected AtomicLong getGauge(final String gaugeName) {
        return gauges.computeIfAbsent(gaugeName, m -> registry.gauge(m, new AtomicLong(0)));
    }

}
