plugins {
    id("org.springframework.boot")
    id("drc-workflow.spring-boot-conventions")
}

// Core dependencies
dependencies {

    // General
    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // Camnunda
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-webapp")
    implementation("org.camunda.bpm:camunda-engine-plugin-spin")
    implementation("org.camunda.spin:camunda-spin-core")
    implementation("org.camunda.spin:camunda-spin-dataformat-json-jackson")

    // Submodules
    implementation(project(":dispute-process-definition"))

    // Metrics support
    implementation("io.micrometer:micrometer-jersey2")
    implementation("io.micrometer:micrometer-registry-datadog")

    // Tracing support
    implementation("io.opentracing.contrib:opentracing-spring-tracer-configuration-starter")
    implementation("io.opentracing:opentracing-api")
    implementation("io.opentracing.contrib:opentracing-spring-cloud-starter") //TODO: Exclude all the dependencies that we are not using.
    implementation("com.datadoghq:dd-trace-api")
    implementation("com.datadoghq:dd-trace-ot")

    testImplementation("io.opentracing:opentracing-mock")

}

// Per profile configuration & dependencies (see buildSrc/main/kotlin/profiles)
val buildProfile: String? by project
apply(plugin = "profile.${buildProfile ?: "default"}")


