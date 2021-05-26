plugins {
    id("io.spring.dependency-management")
}

group = "com.depop"
version = "0.0.1"

// Version management
// NOTE: Spring boot dependency versions are managed by the spring boot BOM
// please see drc-workflow.spring-boot-conventions.gradle.kts
dependencyManagement {
    dependencies {

        // Camunda dependencies
        dependency("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter:7.15.0")
        dependency("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.15.0")

        // Opentracing dependencies
        dependency("io.opentracing.contrib:opentracing-spring-tracer-configuration-starter:0.4.0")
        dependency("io.opentracing:opentracing-api:0.33.0")
        dependency("io.opentracing:opentracing-mock:0.33.0")

    }
}