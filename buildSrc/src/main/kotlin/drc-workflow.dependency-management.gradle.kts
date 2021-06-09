plugins {
    id("io.spring.dependency-management")
}

// Version management
// NOTE: Spring boot dependency versions are managed by the spring boot BOM
// please see drc-workflow.spring-boot-conventions.gradle.kts
dependencyManagement {
    dependencies {

        // Kotlin dependencies
        dependency("org.jetbrains.kotlin:kotlin-reflect:1.5.10")
        dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.10")

        // Camunda dependencies
        dependency("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter:7.15.0")
        dependency("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.15.0")

        // Opentracing dependencies
        dependency("io.opentracing.contrib:opentracing-spring-cloud-starter:0.5.9")
        dependency("io.opentracing.contrib:opentracing-spring-tracer-configuration-starter:0.4.0")
        dependency("io.opentracing:opentracing-api:0.33.0")
        dependency("io.opentracing:opentracing-mock:0.33.0")

        // Datadog dependencies
        dependency("com.datadoghq:dd-trace-api:0.80.0")
        dependency("com.datadoghq:dd-trace-ot:0.80.0")

    }
}