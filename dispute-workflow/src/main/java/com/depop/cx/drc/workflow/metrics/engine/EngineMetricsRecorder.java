package com.depop.cx.drc.workflow.metrics.engine;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class EngineMetricsRecorder {

    private static final Logger log = LoggerFactory.getLogger(EngineMetricsRecorder.class);
    private static final String PREFIX = "depop.service.dispute_workflow.camunda.engine.";

    private final MeterRegistry registry;
    private final Map<String, Counter> metrics = new ConcurrentHashMap<>();
    private List<Tag> commonTags = new ArrayList<>();

    public EngineMetricsRecorder(final MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordMetric(final String name, final long value) {
        final String metricName = PREFIX + name.replace('-', '.');
        log.debug("Recorded metric [{}] with value [{}]", metricName, value);
        getMetric(metricName).increment(value);
    }

    protected Counter getMetric(final String metricName) {
        return metrics.computeIfAbsent(metricName, m -> registry.counter(m, commonTags));
    }

}
