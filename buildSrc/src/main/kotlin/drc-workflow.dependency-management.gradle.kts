plugins {
    id("io.spring.dependency-management")
}

// Version management
dependencyManagement {

    imports {
        // Spring dependencies
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        // Camunda Dependencies
        mavenBom("org.camunda.bpm:camunda-bom:7.22.0")
    }

    configurations.all {
        exclude(group = "org.apache.logging.log4j", module = "log4j-api")
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
    }

    dependencies {

        // GraalVM JS Engine
        dependency("org.graalvm.js:js:24.1.1")
        dependency("org.graalvm.js:js-scriptengine:24.1.1")

        // Security dependencies
        dependency("com.depop:depop-jwt_2.13:0.0.23")
        dependency("com.okta.spring:okta-spring-security-oauth2:3.0.7")

        // Opentracing dependencies
        dependency("io.opentracing.contrib:opentracing-spring-cloud-starter:0.5.9")
        dependency("io.opentracing.contrib:opentracing-spring-tracer-configuration-starter:0.4.0")
        dependency("io.opentracing:opentracing-api:0.33.0")
        dependency("io.opentracing:opentracing-mock:0.33.0")

        // Datadog dependencies
        dependency("com.datadoghq:dd-trace-api:1.42.2")
        dependency("com.datadoghq:dd-trace-ot:1.42.2")

        // General dependencies
        dependency("io.github.oshai:kotlin-logging-jvm:7.0.0")
        dependency("com.nimbusds:nimbus-jose-jwt:9.47")
        dependency("commons-fileupload:commons-fileupload:1.5")
        dependency("com.google.guava:guava:33.3.1-jre")

        // Snyk version overrides
        dependency("commons-io:commons-io:2.17.0")
        dependency("io.netty:netty-common:4.1.115.Final")
        dependency("org.xmlunit:xmlunit-core:2.10.0")

    }

}
