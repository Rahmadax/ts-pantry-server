plugins {
    id("io.spring.dependency-management")
}

// Version management
dependencyManagement {

    imports {
        // Camunda Dependencies
        mavenBom("org.camunda.bpm:camunda-bom:7.16.0")

        // Spring dependencies
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES) {
            bomProperty("kotlin.version", "1.9.24")
            bomProperty("spring-framework.version", "5.3.39")
            bomProperty("spring-security.version", "5.7.12")
            bomProperty("jackson-bom.version", "2.17.2")
            bomProperty("logback.version", "1.2.13")
            bomProperty("snakeyaml.version", "2.3")
            bomProperty("h2.version", "2.3.232")
            bomProperty("json-path.version", "2.9.0")
            bomProperty("xmlunit2.version", "2.10.0")
        }
    }

    configurations.all {
        exclude(group = "org.apache.logging.log4j", module = "log4j-api")
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
    }

    dependencies {


        // GraalVM JS Engine
        // https://security.snyk.io/vuln/SNYK-JAVA-ORGGRAALVMSDK-6163607
        dependency("org.graalvm.js:js:21.3.11")
        dependency("org.graalvm.js:js-scriptengine:21.3.11")

        // Security dependencies
        dependency("com.depop:depop-jwt_2.13:0.0.23")
        dependency("com.okta.spring:okta-spring-security-oauth2:1.1.0")

        // Opentracing dependencies
        dependency("io.opentracing.contrib:opentracing-spring-cloud-starter:0.5.9")
        dependency("io.opentracing.contrib:opentracing-spring-tracer-configuration-starter:0.4.0")
        dependency("io.opentracing:opentracing-api:0.33.0")
        dependency("io.opentracing:opentracing-mock:0.33.0")

        // Datadog dependencies
        dependency("com.datadoghq:dd-trace-api:1.0.0")
        dependency("com.datadoghq:dd-trace-ot:1.0.0")

        // General dependencies
        dependency("io.github.microutils:kotlin-logging-jvm:3.0.5")

        dependency("com.nimbusds:nimbus-jose-jwt:9.41.1")

        dependency("commons-fileupload:commons-fileupload:1.5")

    }

}
