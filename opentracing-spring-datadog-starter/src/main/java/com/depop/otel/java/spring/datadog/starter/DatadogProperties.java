package com.depop.otel.java.spring.datadog.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;


@ConfigurationProperties("opentracing.datadog")
public class DatadogProperties {


    public Properties getJavaProperties() {
        return null;
    }

}
