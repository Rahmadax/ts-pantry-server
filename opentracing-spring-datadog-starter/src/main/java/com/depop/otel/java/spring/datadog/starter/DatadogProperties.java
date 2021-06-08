package com.depop.otel.java.spring.datadog.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.util.ObjectUtils;

import java.util.Properties;

/**
 * Configuration properties for Datadog Opentracing in Spring Boot.
 * <p>
 * Users should refer to Datadog spring integration documentation for complete descriptions of these
 * properties.
 * <p>
 * https://docs.datadoghq.com/tracing/setup_overview/setup/java?tab=containers#configuration
 *
 * @author Tom Greasley
 * @since 1.0.0
 */
@ConfigurationProperties("opentracing.datadog")
public class DatadogProperties {

    /**
     * Enables/Disabled the autoconfiguration of datadog support for opentracing.
     * Datadog Property: N/A
     * Default: true
     **/
    private Boolean enabled = Boolean.TRUE;

    /**
     * The name of a set of processes that do the same job. Used for grouping stats for your application. Available for versions 0.50.0+.
     * Datadog Property: dd.service
     * Default: unnamed-java-app
     **/
    private String service;

    /**
     * A list of default tags to be added to every span, profile, and JMX metric. If DD_ENV or DD_VERSION is used, it will override any env or version tag defined in DD_TAGS. Available for versions 0.50.0+.
     * Datadog Property: dd.tags
     * Default: null
     * Example: layer:api,team:intake
     **/
    private String tags;

    /**
     * Your application environment (e.g. production, staging, etc.). Available for versions 0.48+.
     * Datadog Property: dd.env
     * Default: none
     **/
    private String env;

    /**
     * Your application version (e.g. 2.5, 202003181415, 1.3-alpha, etc.). Available for versions 0.48+.
     * Datadog Property: dd.version
     * Default: null
     **/
    private String version;

    /**
     * Enabled automatic MDC key injection for Datadog trace and span IDs. See Advanced Usage for details.
     * Datadog Property: dd.logs.injection
     * Default: false
     **/
    private Boolean logsInjection;

    /**
     * Dynamically rename services via configuration. Useful for making databases have distinct names across different services.
     * Datadog Property: dd.service.mapping
     * Default: null
     * Example: mysql:my-mysql-service-name-db, postgres:my-postgres-service-name-db
     **/
    private String serviceMapping;

    /**
     * Default value sends traces to the Agent. Configuring with LoggingWriter instead writes traces out to the console.
     * Datadog Property: dd.writer.type
     * Default: DDAgentWriter
     **/
    private String writerType;

    /**
     * Hostname for where to send traces to. If using a containerized environment, configure this to be the host IP. See Tracing Docker Applications for more details.
     * Datadog Property: dd.agent.host
     * Default: localhost
     **/
    private String agentHost;

    /**
     * By default the tracing client detects if a GlobalTracer is being loaded and dynamically registers a tracer into it. By turning this to false, this removes any tracer dependency on OpenTracing.
     * Datadog Property: dd.integration.opentracing.enabled
     * Default: true
     **/
    private Boolean integrationOpentracingEnabled;

    /**
     * By default the Hystrix group, command, and circuit state tags are not enabled. This property enables them.
     * Datadog Property: dd.hystrix.tags.enabled
     * Default: false
     **/
    private Boolean hystrixTagsEnabled;

    @NestedConfigurationProperty
    private TraceProperties trace = new TraceProperties();

    @NestedConfigurationProperty
    private HttpProperties http = new HttpProperties();

    @NestedConfigurationProperty
    private JmxFetchProperties jmxfetch = new JmxFetchProperties();

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getLogsInjection() {
        return logsInjection;
    }

    public void setLogsInjection(Boolean logsInjection) {
        this.logsInjection = logsInjection;
    }

    public String getServiceMapping() {
        return serviceMapping;
    }

    public void setServiceMapping(String serviceMapping) {
        this.serviceMapping = serviceMapping;
    }

    public String getWriterType() {
        return writerType;
    }

    public void setWriterType(String writerType) {
        this.writerType = writerType;
    }

    public String getAgentHost() {
        return agentHost;
    }

    public void setAgentHost(String agentHost) {
        this.agentHost = agentHost;
    }

    public Boolean getIntegrationOpentracingEnabled() {
        return integrationOpentracingEnabled;
    }

    public void setIntegrationOpentracingEnabled(Boolean integrationOpentracingEnabled) {
        this.integrationOpentracingEnabled = integrationOpentracingEnabled;
    }

    public Boolean getHystrixTagsEnabled() {
        return hystrixTagsEnabled;
    }

    public void setHystrixTagsEnabled(Boolean hystrixTagsEnabled) {
        this.hystrixTagsEnabled = hystrixTagsEnabled;
    }

    public TraceProperties getTrace() {
        return trace;
    }

    public void setTrace(TraceProperties trace) {
        this.trace = trace;
    }

    public HttpProperties getHttp() {
        return http;
    }

    public void setHttp(HttpProperties http) {
        this.http = http;
    }

    public JmxFetchProperties getJmxfetch() {
        return jmxfetch;
    }

    public void setJmxfetch(JmxFetchProperties jmxfetch) {
        this.jmxfetch = jmxfetch;
    }

    /**
     * Maps the configured properties to a {@link Properties} bag.
     *
     * @return a {@link Properties} containing all the configured properties, using the datadog property names.
     */
    public Properties buildProperties() {
        final var properties = new SafeProperties();
        final var map = PropertyMapper.get().alwaysApplyingWhenNonNull();

        map.from(this::getService).to(properties.as(DatadogPropertyNames.DD_SERVICE));
        map.from(this::getTags).to(properties.as(DatadogPropertyNames.DD_TAGS));
        map.from(this::getEnv).to(properties.as(DatadogPropertyNames.DD_ENV));
        map.from(this::getVersion).to(properties.as(DatadogPropertyNames.DD_VERSION));
        map.from(this::getLogsInjection).to(properties.as(DatadogPropertyNames.DD_LOGS_INJECTION));
        map.from(this::getServiceMapping).to(properties.as(DatadogPropertyNames.DD_SERVICE_MAPPING));
        map.from(this::getWriterType).to(properties.as(DatadogPropertyNames.DD_WRITER_TYPE));
        map.from(this::getAgentHost).to(properties.as(DatadogPropertyNames.DD_AGENT_HOST));
        map.from(this::getIntegrationOpentracingEnabled).to(properties.as(DatadogPropertyNames.DD_INTEGRATION_OPENTRACING_ENABLED));
        map.from(this::getHystrixTagsEnabled).to(properties.as(DatadogPropertyNames.DD_HYSTRIX_TAGS_ENABLED));

        return properties
                .with(getTrace().buildProperties())
                .with(getHttp().buildProperties())
                .with(getJmxfetch().buildProperties());
    }

    public static class TraceProperties {

        /**
         * Optional path to a file where configuration properties are provided one per each line. For instance, the file path can be provided as via -Ddd.trace.config=<FILE_PATH>.properties, with setting the service name in the file with dd.service=<SERVICE_NAME>
         * Datadog Property: dd.trace.config
         * Default: null
         **/
        private String config;

        /**
         * When false tracing agent is disabled.
         * Datadog Property: dd.trace.enabled
         * Default: true
         **/
        private Boolean enabled;


        /**
         * Port number the Agent is listening on for configured host.
         * Datadog Property: dd.trace.agent.port
         * Default: 8126
         **/
        private Integer agentPort;

        /**
         * This can be used to direct trace traffic to a proxy, to later be sent to a remote Datadog Agent.
         * Datadog Property: dd.trace.agent.unix.domain.socket
         * Default: null
         **/
        private String agentUnixDomainSocket;

        /**
         * The URL to send traces to. This can start with http:// to connect using HTTP or with unix:// to use a Unix Domain Socket. When set this takes precedence over DD_AGENT_HOST and DD_TRACE_AGENT_PORT. Available for versions 0.65+.
         * Datadog Property: dd.trace.agent.url
         * Default: null
         **/
        private String agentUrl;

        /**
         * Timeout in seconds for network interactions with the Datadog Agent.
         * Datadog Property: dd.trace.agent.timeout
         * Default: 10
         **/
        private Integer agentTimeout;

        /**
         * A map of header keys to tag names. Automatically apply header values as tags on traces.
         * Datadog Property: dd.trace.header.tags
         * Default: null
         * Example: CASE-insensitive-Header:my-tag-name,User-ID:userId
         **/
        private String headerTags;

        /**
         * A list of method annotations to treat as @Trace.
         * Datadog Property: dd.trace.annotations
         * Default: (listed here)
         * Example: com.some.Trace;io.other.Trace
         **/
        private String annotations;


        /**
         * List of class/interface and methods to trace. Similar to adding @Trace, but without changing code. Note: The wildcard method support ([*]) does not accommodate constructors, getters, setters, synthetic, toString, equals, hashcode, or finalizer method calls
         * Datadog Property: dd.trace.methods
         * Default: null
         * Example: "package.ClassName[method1,method2,...];AnonymousClass$1[call];package.ClassName[*]"
         **/
        private String methods;

        /**
         * A list of fully qualified classes (that may end with a wildcard to denote a prefix) which will be ignored (not modified) by the tracer. Must use the jvm internal representation for names (eg package.ClassName$Nested and not package.ClassName.Nested)
         * Datadog Property: dd.trace.classes.exclude
         * Default: null
         * Example: package.ClassName,package.ClassName$Nested,package.Foo*,package.other.*
         **/
        private String classesExclude;

        /**
         * Set a number of partial spans to flush on. Useful to reduce memory overhead when dealing with heavy traffic or long running traces.
         * Datadog Property: dd.trace.partial.flush.min.spans
         * Default: 1000
         **/
        private Long partialFlushMinSpans;

        /**
         * Used to rename spans to be identified with the corresponding service tag
         * Datadog Property: dd.trace.split-by-tags
         * Default: null
         * Example: aws.service
         **/
        private String splitByTags;

        /**
         * When set to true db spans get assigned the instance name as the service name
         * Datadog Property: dd.trace.db.client.split-by-instance
         * Default: false
         **/
        private Boolean dbClientSplitByInstance;

        /**
         * When false, informational startup logging is disabled. Available for versions 0.64+.
         * Datadog Property: dd.trace.startup.logs
         * Default: true
         **/
        private Boolean startupLogs;

        @NestedConfigurationProperty
        private HealthMetricsProperties healthMetrics = new HealthMetricsProperties();

        @NestedConfigurationProperty
        private ServletProperties servlet = new ServletProperties();

        public String getConfig() {
            return config;
        }

        public void setConfig(String config) {
            this.config = config;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public Integer getAgentPort() {
            return agentPort;
        }

        public void setAgentPort(Integer agentPort) {
            this.agentPort = agentPort;
        }

        public String getAgentUnixDomainSocket() {
            return agentUnixDomainSocket;
        }

        public void setAgentUnixDomainSocket(String agentUnixDomainSocket) {
            this.agentUnixDomainSocket = agentUnixDomainSocket;
        }

        public String getAgentUrl() {
            return agentUrl;
        }

        public void setAgentUrl(String agentUrl) {
            this.agentUrl = agentUrl;
        }

        public Integer getAgentTimeout() {
            return agentTimeout;
        }

        public void setAgentTimeout(Integer agentTimeout) {
            this.agentTimeout = agentTimeout;
        }

        public String getHeaderTags() {
            return headerTags;
        }

        public void setHeaderTags(String headerTags) {
            this.headerTags = headerTags;
        }

        public String getAnnotations() {
            return annotations;
        }

        public void setAnnotations(String annotations) {
            this.annotations = annotations;
        }

        public String getMethods() {
            return methods;
        }

        public void setMethods(String methods) {
            this.methods = methods;
        }

        public String getClassesExclude() {
            return classesExclude;
        }

        public void setClassesExclude(String classesExclude) {
            this.classesExclude = classesExclude;
        }

        public Long getPartialFlushMinSpans() {
            return partialFlushMinSpans;
        }

        public void setPartialFlushMinSpans(Long partialFlushMinSpans) {
            this.partialFlushMinSpans = partialFlushMinSpans;
        }

        public String getSplitByTags() {
            return splitByTags;
        }

        public void setSplitByTags(String splitByTags) {
            this.splitByTags = splitByTags;
        }

        public Boolean getDbClientSplitByInstance() {
            return dbClientSplitByInstance;
        }

        public void setDbClientSplitByInstance(Boolean dbClientSplitByInstance) {
            this.dbClientSplitByInstance = dbClientSplitByInstance;
        }

        public Boolean getStartupLogs() {
            return startupLogs;
        }

        public void setStartupLogs(Boolean startupLogs) {
            this.startupLogs = startupLogs;
        }

        public HealthMetricsProperties getHealthMetrics() {
            return healthMetrics;
        }

        public void setHealthMetrics(HealthMetricsProperties healthMetrics) {
            this.healthMetrics = healthMetrics;
        }

        public ServletProperties getServlet() {
            return servlet;
        }

        public void setServlet(ServletProperties servlet) {
            this.servlet = servlet;
        }

        /**
         * Maps the configured properties to a {@link Properties} bag.
         *
         * @return a {@link Properties} containing all the configured properties, using the datadog property names.
         */
        public Properties buildProperties() {
            final var properties = new SafeProperties();
            final var map = PropertyMapper.get().alwaysApplyingWhenNonNull();

            map.from(this::getConfig).to(properties.as(DatadogPropertyNames.DD_TRACE_CONFIG));
            map.from(this::getEnabled).to(properties.as(DatadogPropertyNames.DD_TRACE_ENABLED));
            map.from(this::getAgentPort).to(properties.as(DatadogPropertyNames.DD_TRACE_AGENT_PORT));
            map.from(this::getAgentUnixDomainSocket).to(properties.as(DatadogPropertyNames.DD_TRACE_AGENT_UNIX_DOMAIN_SOCKET));
            map.from(this::getAgentUrl).to(properties.as(DatadogPropertyNames.DD_TRACE_AGENT_URL));
            map.from(this::getAgentTimeout).to(properties.as(DatadogPropertyNames.DD_TRACE_AGENT_TIMEOUT));
            map.from(this::getHeaderTags).to(properties.as(DatadogPropertyNames.DD_TRACE_HEADER_TAGS));
            map.from(this::getAnnotations).to(properties.as(DatadogPropertyNames.DD_TRACE_ANNOTATIONS));
            map.from(this::getMethods).to(properties.as(DatadogPropertyNames.DD_TRACE_METHODS));
            map.from(this::getClassesExclude).to(properties.as(DatadogPropertyNames.DD_TRACE_CLASSES_EXCLUDE));
            map.from(this::getPartialFlushMinSpans).to(properties.as(DatadogPropertyNames.DD_TRACE_PARTIAL_FLUSH_MIN_SPANS));
            map.from(this::getSplitByTags).to(properties.as(DatadogPropertyNames.DD_TRACE_SPLIT_BY_TAGS));
            map.from(this::getDbClientSplitByInstance).to(properties.as(DatadogPropertyNames.DD_TRACE_DB_CLIENT_SPLIT_BY_INSTANCE));
            map.from(this::getStartupLogs).to(properties.as(DatadogPropertyNames.DD_TRACE_STARTUP_LOGS));

            return properties.with(getServlet().buildProperties()).with(getHealthMetrics().buildProperties());
        }

    }

    public static class HealthMetricsProperties {

        /**
         * When set to true sends tracer health metrics
         * Datadog Property: dd.trace.health.metrics.enabled
         * Default: false
         **/
        private Boolean enabled;

        /**
         * Statsd host to send health metrics to
         * Datadog Property: dd.trace.health.metrics.statsd.host
         * Default: Same as dd.jmxfetch.statsd.host
         **/
        private String statsdHost;

        /**
         * Statsd port to send health metrics to
         * Datadog Property: dd.trace.health.metrics.statsd.port
         * Default: Same as dd.jmxfetch.statsd.port
         **/
        private Integer statsdPort;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getStatsdHost() {
            return statsdHost;
        }

        public void setStatsdHost(String statsdHost) {
            this.statsdHost = statsdHost;
        }

        public Integer getStatsdPort() {
            return statsdPort;
        }

        public void setStatsdPort(Integer statsdPort) {
            this.statsdPort = statsdPort;
        }

        /**
         * Maps the configured properties to a {@link Properties} bag.
         *
         * @return a {@link Properties} containing all the configured properties, using the datadog property names.
         */
        public Properties buildProperties() {
            final var properties = new SafeProperties();
            final var map = PropertyMapper.get().alwaysApplyingWhenNonNull();

            map.from(this::getEnabled).to(properties.as(DatadogPropertyNames.DD_TRACE_HEALTH_METRICS_ENABLED));
            map.from(this::getStatsdHost).to(properties.as(DatadogPropertyNames.DD_TRACE_HEALTH_METRICS_STATSD_HOST));
            map.from(this::getStatsdPort).to(properties.as(DatadogPropertyNames.DD_TRACE_HEALTH_METRICS_STATSD_PORT));

            return properties;
        }

    }

    public static class ServletProperties {

        /**
         * By default, long running asynchronous requests will be marked as an error, setting this value to false allows to mark all timeouts as successful requests.
         * Datadog Property: dd.trace.servlet.async-timeout.error
         * Default: true
         **/
        private Boolean asyncTimeoutError;

        /**
         * When true, user principal is collected. Available for versions 0.61+.
         * Datadog Property: dd.trace.servlet.principal.enabled
         * Default: false
         */
        private Boolean principalEnabled;

        public Boolean getAsyncTimeoutError() {
            return asyncTimeoutError;
        }

        public void setAsyncTimeoutError(Boolean asyncTimeoutError) {
            this.asyncTimeoutError = asyncTimeoutError;
        }

        public Boolean getPrincipalEnabled() {
            return principalEnabled;
        }

        public void setPrincipalEnabled(Boolean principalEnabled) {
            this.principalEnabled = principalEnabled;
        }

        /**
         * Maps the configured properties to a {@link Properties} bag.
         *
         * @return a {@link Properties} containing all the configured properties, using the datadog property names.
         */
        public Properties buildProperties() {
            final var properties = new SafeProperties();
            final var map = PropertyMapper.get().alwaysApplyingWhenNonNull();

            map.from(this::getAsyncTimeoutError).to(properties.as(DatadogPropertyNames.DD_TRACE_SERVLET_ASYNC_TIMEOUT_ERROR));
            map.from(this::getPrincipalEnabled).to(properties.as(DatadogPropertyNames.DD_TRACE_SERVLET_PRINCIPAL_ENABLED));

            return properties;
        }

    }

    public static class HttpProperties {

        @NestedConfigurationProperty
        private HttpClientProperties client = new HttpClientProperties();

        @NestedConfigurationProperty
        private HttpServerProperties server = new HttpServerProperties();

        public HttpClientProperties getClient() {
            return client;
        }

        public void setClient(HttpClientProperties client) {
            this.client = client;
        }

        public HttpServerProperties getServer() {
            return server;
        }

        public void setServer(HttpServerProperties server) {
            this.server = server;
        }

        /**
         * Maps the configured properties to a {@link Properties} bag.
         *
         * @return a {@link Properties} containing all the configured properties, using the datadog property names.
         */
        public SafeProperties buildProperties() {
            final var properties = new SafeProperties();
            return properties.with(getClient().buildProperties()).with(getServer().buildProperties());
        }

    }

    public static class HttpClientProperties {

        /**
         * When set to true query string parameters and fragment get added to web client spans
         * Datadog Property: dd.http.client.tag.query-string
         * Default: false
         **/
        private Boolean tagQueryString;

        /**
         * A range of errors can be accepted. By default 4xx errors are reported as errors for http clients. This configuration overrides that. Ex. dd.http.client.error.statuses=400-403,405,410-499
         * Datadog Property: dd.http.client.error.statuses
         * Default: 400-499
         **/
        private String errorStatuses;

        public Boolean getTagQueryString() {
            return tagQueryString;
        }

        public void setTagQueryString(Boolean tagQueryString) {
            this.tagQueryString = tagQueryString;
        }

        public String getErrorStatuses() {
            return errorStatuses;
        }

        public void setErrorStatuses(String errorStatuses) {
            this.errorStatuses = errorStatuses;
        }

        /**
         * Maps the configured properties to a {@link Properties} bag.
         *
         * @return a {@link Properties} containing all the configured properties, using the datadog property names.
         */
        public Properties buildProperties() {
            final var properties = new SafeProperties();
            final var map = PropertyMapper.get().alwaysApplyingWhenNonNull();

            map.from(this::getTagQueryString).to(properties.as(DatadogPropertyNames.DD_HTTP_CLIENT_TAG_QUERY_STRING));
            map.from(this::getErrorStatuses).to(properties.as(DatadogPropertyNames.DD_HTTP_CLIENT_ERROR_STATUSES));

            return properties;
        }

    }

    public static class HttpServerProperties {

        /**
         * When set to true query string parameters and fragment get added to web server spans
         * Datadog Property: dd.http.server.tag.query-string
         * Default: false
         **/
        private Boolean tagQueryString;

        /**
         * A range of errors can be accepted. By default 5xx status codes are reported as errors for http servers. This configuration overrides that. Ex. dd.http.server.error.statuses=500,502-599
         * Datadog Property: dd.http.server.error.statuses
         * Default: 500-599
         **/
        private String errorStatuses;

        public Boolean getTagQueryString() {
            return tagQueryString;
        }

        public void setTagQueryString(Boolean tagQueryString) {
            this.tagQueryString = tagQueryString;
        }

        public String getErrorStatuses() {
            return errorStatuses;
        }

        public void setErrorStatuses(String errorStatuses) {
            this.errorStatuses = errorStatuses;
        }

        /**
         * Maps the configured properties to a {@link Properties} bag.
         *
         * @return a {@link Properties} containing all the configured properties, using the datadog property names.
         */
        public Properties buildProperties() {
            final var properties = new SafeProperties();
            final var map = PropertyMapper.get().alwaysApplyingWhenNonNull();

            map.from(this::getTagQueryString).to(properties.as(DatadogPropertyNames.DD_HTTP_SERVER_TAG_QUERY_STRING));
            map.from(this::getErrorStatuses).to(properties.as(DatadogPropertyNames.DD_HTTP_SERVER_ERROR_STATUSES));

            return properties;
        }

    }

    public static class JmxFetchProperties {

        /**
         * Enable collection of JMX metrics by Java Tracing Agent.
         * Datadog Property: dd.jmxfetch.enabled
         * Default: true
         **/
        private Boolean enabled;

        /**
         * Additional metrics configuration file for JMX metrics collection. The Java Agent looks for jvm_direct:true in the instance section in the yaml file to change configuration.
         * Datadog Property: dd.jmxfetch.config
         * Default: null
         * Example: activemq.d/conf.yaml,jmx.d/conf.yaml
         **/
        private String config;


        /**
         * Additional configuration directory for JMX metrics collection. The Java Agent looks for jvm_direct:true in the instance section in the yaml file to change configuration.
         * Datadog Property: dd.jmxfetch.config.dir
         * Default: null
         * Example: /opt/datadog-agent/etc/conf.d
         **/
        private String configDir;

        /**
         * How often to send JMX metrics (in ms).
         * Datadog Property: dd.jmxfetch.check-period
         * Default: 1500
         **/
        private Integer checkPeriod;

        /**
         * How often to refresh list of available JMX beans (in seconds).
         * Datadog Property: dd.jmxfetch.refresh-beans-period
         * Default: 600
         **/
        private Integer refreshBeansPeriod;

        /**
         * Statsd host to send JMX metrics to. If you are using Unix Domain Sockets, use an argument like ‘unix://PATH_TO_UDS_SOCKET’. Example: unix:///var/datadog-agent/dsd.socket
         * Datadog Property: dd.jmxfetch.statsd.host
         * Default: Same as agent.host
         **/
        private String statsdHost;

        /**
         * StatsD port to send JMX metrics to. If you are using Unix Domain Sockets, input 0.
         * dd.jmxfetch.statsd.port
         * Default: 8125
         **/
        private String statsdPort;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getConfig() {
            return config;
        }

        public void setConfig(String config) {
            this.config = config;
        }

        public String getConfigDir() {
            return configDir;
        }

        public void setConfigDir(String configDir) {
            this.configDir = configDir;
        }

        public Integer getCheckPeriod() {
            return checkPeriod;
        }

        public void setCheckPeriod(Integer checkPeriod) {
            this.checkPeriod = checkPeriod;
        }

        public Integer getRefreshBeansPeriod() {
            return refreshBeansPeriod;
        }

        public void setRefreshBeansPeriod(Integer refreshBeansPeriod) {
            this.refreshBeansPeriod = refreshBeansPeriod;
        }

        public String getStatsdHost() {
            return statsdHost;
        }

        public void setStatsdHost(String statsdHost) {
            this.statsdHost = statsdHost;
        }

        public String getStatsdPort() {
            return statsdPort;
        }

        public void setStatsdPort(String statsdPort) {
            this.statsdPort = statsdPort;
        }

        /**
         * Maps the configured properties to a {@link Properties} bag.
         *
         * @return a {@link Properties} containing all the configured properties, using the datadog property names.
         */
        public Properties buildProperties() {
            final var properties = new SafeProperties();
            final var map = PropertyMapper.get().alwaysApplyingWhenNonNull();

            map.from(this::getEnabled).to(properties.as(DatadogPropertyNames.DD_JMXFETCH_ENABLED));
            map.from(this::getConfig).to(properties.as(DatadogPropertyNames.DD_JMXFETCH_CONFIG));
            map.from(this::getConfigDir).to(properties.as(DatadogPropertyNames.DD_JMXFETCH_CONFIG_DIR));
            map.from(this::getCheckPeriod).to(properties.as(DatadogPropertyNames.DD_JMXFETCH_CHECK_PERIOD));
            map.from(this::getRefreshBeansPeriod).to(properties.as(DatadogPropertyNames.DD_JMXFETCH_REFRESH_BEANS_PERIOD));
            map.from(this::getStatsdHost).to(properties.as(DatadogPropertyNames.DD_JMXFETCH_STATSD_HOST));
            map.from(this::getStatsdPort).to(properties.as(DatadogPropertyNames.DD_JMXFETCH_STATSD_PORT));

            return properties;
        }

    }

    // Utility class for mapping these spring boot properties into a java properties bag.
    private static class SafeProperties extends Properties {

        <V> java.util.function.Consumer<V> as(final DatadogPropertyNames property) {
            return value -> {
                if (!ObjectUtils.isEmpty(value)) {
                    setProperty(property.getPropertyName(), value.toString());
                }
            };
        }

        SafeProperties with(final Properties properties) {
            if (!ObjectUtils.isEmpty(properties)) {
                putAll(properties);
            }
            return this;
        }
    }

}
