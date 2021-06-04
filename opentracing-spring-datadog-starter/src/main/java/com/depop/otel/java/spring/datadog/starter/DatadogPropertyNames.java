package com.depop.otel.java.spring.datadog.starter;

public enum DatadogPropertyNames {

    // Core
    DD_SERVICE("dd.service"),
    DD_TAGS("dd.tags"),
    DD_ENV("dd.env"),
    DD_VERSION("dd.version"),
    DD_LOGS_INJECTION("dd.logs.injection"),
    DD_SERVICE_MAPPING("dd.service.mapping"),
    DD_WRITER_TYPE("dd.writer.type"),
    DD_AGENT_HOST("dd.agent.host"),
    DD_INTEGRATION_OPENTRACING_ENABLED("dd.integration.opentracing.enabled"),
    DD_HYSTRIX_TAGS_ENABLED("dd.hystrix.tags.enabled"),

    // Trace
    DD_TRACE_CONFIG("dd.trace.config"),
    DD_TRACE_ENABLED("dd.trace.enabled"),
    DD_TRACE_AGENT_PORT("dd.trace.agent.port"),
    DD_TRACE_AGENT_UNIX_DOMAIN_SOCKET("dd.trace.agent.unix.domain.socket"),
    DD_TRACE_AGENT_URL("dd.trace.agent.url"),
    DD_TRACE_AGENT_TIMEOUT("dd.trace.agent.timeout"),
    DD_TRACE_HEADER_TAGS("dd.trace.header.tags"),
    DD_TRACE_ANNOTATIONS("dd.trace.annotations"),
    DD_TRACE_METHODS("dd.trace.methods"),
    DD_TRACE_CLASSES_EXCLUDE("dd.trace.classes.exclude"),
    DD_TRACE_PARTIAL_FLUSH_MIN_SPANS("dd.trace.partial.flush.min.spans"),
    DD_TRACE_SPLIT_BY_TAGS("dd.trace.split-by-tags"),
    DD_TRACE_DB_CLIENT_SPLIT_BY_INSTANCE("dd.trace.db.client.split-by-instance"),
    DD_TRACE_STARTUP_LOGS("dd.trace.startup.logs"),

    // Trace Health Metrics
    DD_TRACE_HEALTH_METRICS_ENABLED("dd.trace.health.metrics.enabled"),
    DD_TRACE_HEALTH_METRICS_STATSD_HOST("dd.trace.health.metrics.statsd.host"),
    DD_TRACE_HEALTH_METRICS_STATSD_PORT("dd.trace.health.metrics.statsd.port"),

    // Trace Servlet
    DD_TRACE_SERVLET_ASYNC_TIMEOUT_ERROR("dd.trace.servlet.async-timeout.error"),
    DD_TRACE_SERVLET_PRINCIPAL_ENABLED("dd.trace.servlet.principal.enabled"),

    // HTTP Client
    DD_HTTP_CLIENT_TAG_QUERY_STRING("dd.http.client.tag.query-string"),
    DD_HTTP_CLIENT_ERROR_STATUSES("dd.http.client.error.statuses"),

    // HTTP Server
    DD_HTTP_SERVER_TAG_QUERY_STRING("dd.http.server.tag.query-string"),
    DD_HTTP_SERVER_ERROR_STATUSES("dd.http.server.error.statuses"),

    // JMX
    DD_JMXFETCH_ENABLED("dd.jmxfetch.enabled"),
    DD_JMXFETCH_CONFIG("dd.jmxfetch.config"),
    DD_JMXFETCH_CONFIG_DIR("dd.jmxfetch.config.dir"),
    DD_JMXFETCH_CHECK_PERIOD("dd.jmxfetch.check-period"),
    DD_JMXFETCH_REFRESH_BEANS_PERIOD("dd.jmxfetch.refresh-beans-period"),
    DD_JMXFETCH_STATSD_HOST("dd.jmxfetch.statsd.host"),
    DD_JMXFETCH_STATSD_PORT("dd.jmxfetch.statsd.port");

    final String propertyName;

    DatadogPropertyNames(final String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
