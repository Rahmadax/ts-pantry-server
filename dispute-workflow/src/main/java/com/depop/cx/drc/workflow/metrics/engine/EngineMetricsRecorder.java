package com.depop.cx.drc.workflow.metrics.engine;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EngineMetricsRecorder {

    private static final Logger log = LoggerFactory.getLogger(EngineMetricsRecorder.class);

    private final String prefix;
    private final MeterRegistry registry;
    private final Map<String, Counter> metrics = new ConcurrentHashMap<>();

    public EngineMetricsRecorder(final MeterRegistry registry, final String prefix) {
        this.registry = registry;
        this.prefix = prefix;
    }

    public void recordMetric(final String name, final long value) {
        final String metricName = this.prefix + name.replace('-', '.');
        log.debug("Recorded metric [{}] with value [{}]", metricName, value);
        getMetric(metricName).increment(value);
    }

    protected Counter getMetric(final String metricName) {
        return metrics.computeIfAbsent(metricName, registry::counter);
    }

}
