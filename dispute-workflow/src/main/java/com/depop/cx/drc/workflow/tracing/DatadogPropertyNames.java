package com.depop.cx.drc.workflow.tracing;

public enum DatadogPropertyNames {

    // Core
    DD_SERVICE("service"),
    DD_TAGS("tags"),
    DD_ENV("env"),
    DD_VERSION("version"),
    DD_LOGS_INJECTION("logs.injection"),
    DD_SERVICE_MAPPING("service.mapping"),
    DD_WRITER_TYPE("writer.type"),
    DD_AGENT_HOST("agent.host"),
    DD_INTEGRATION_OPENTRACING_ENABLED("integration.opentracing.enabled"),
    DD_HYSTRIX_TAGS_ENABLED("hystrix.tags.enabled"),

    // Trace
    DD_TRACE_CONFIG("trace.config"),
    DD_TRACE_ENABLED("trace.enabled"),
    DD_TRACE_AGENT_PORT("trace.agent.port"),
    DD_TRACE_AGENT_UNIX_DOMAIN_SOCKET("trace.agent.unix.domain.socket"),
    DD_TRACE_AGENT_URL("trace.agent.url"),
    DD_TRACE_AGENT_TIMEOUT("trace.agent.timeout"),
    DD_TRACE_HEADER_TAGS("trace.header.tags"),
    DD_TRACE_ANNOTATIONS("trace.annotations"),
    DD_TRACE_METHODS("trace.methods"),
    DD_TRACE_CLASSES_EXCLUDE("trace.classes.exclude"),
    DD_TRACE_PARTIAL_FLUSH_MIN_SPANS("trace.partial.flush.min.spans"),
    DD_TRACE_SPLIT_BY_TAGS("trace.split-by-tags"),
    DD_TRACE_DB_CLIENT_SPLIT_BY_INSTANCE("trace.db.client.split-by-instance"),
    DD_TRACE_STARTUP_LOGS("trace.startup.logs"),

    // Trace Health Metrics
    DD_TRACE_HEALTH_METRICS_ENABLED("trace.health.metrics.enabled"),
    DD_TRACE_HEALTH_METRICS_STATSD_HOST("trace.health.metrics.statsd.host"),
    DD_TRACE_HEALTH_METRICS_STATSD_PORT("trace.health.metrics.statsd.port"),

    // Trace Servlet
    DD_TRACE_SERVLET_ASYNC_TIMEOUT_ERROR("trace.servlet.async-timeout.error"),
    DD_TRACE_SERVLET_PRINCIPAL_ENABLED("trace.servlet.principal.enabled"),

    // HTTP Client
    DD_HTTP_CLIENT_TAG_QUERY_STRING("http.client.tag.query-string"),
    DD_HTTP_CLIENT_ERROR_STATUSES("http.client.error.statuses"),

    // HTTP Server
    DD_HTTP_SERVER_TAG_QUERY_STRING("http.server.tag.query-string"),
    DD_HTTP_SERVER_ERROR_STATUSES("http.server.error.statuses"),

    // JMX
    DD_JMXFETCH_ENABLED("jmxfetch.enabled"),
    DD_JMXFETCH_CONFIG("jmxfetch.config"),
    DD_JMXFETCH_CONFIG_DIR("jmxfetch.config.dir"),
    DD_JMXFETCH_CHECK_PERIOD("jmxfetch.check-period"),
    DD_JMXFETCH_REFRESH_BEANS_PERIOD("jmxfetch.refresh-beans-period"),
    DD_JMXFETCH_STATSD_HOST("jmxfetch.statsd.host"),
    DD_JMXFETCH_STATSD_PORT("jmxfetch.statsd.port");

    final String propertyName;

    DatadogPropertyNames(final String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
