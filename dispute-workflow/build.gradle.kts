plugins {
    id("org.springframework.boot")
    id("drc-workflow.spring-boot-conventions")
}

// Core dependencies
dependencies {

    // General
    implementation("com.google.guava:guava")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jersey")

    // Web support
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("jakarta.servlet:jakarta.servlet-api")

    // Session
    implementation("org.springframework.session:spring-session-jdbc")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // Camunda
    runtimeOnly("org.glassfish.jaxb:jaxb-runtime")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest"){
        exclude(group = "com.sun.xml.bind", module = "jaxb-impl")
    }
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-webapp"){
        exclude(group = "com.sun.xml.bind", module = "jaxb-impl")
    }
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-test"){
        exclude(group = "com.sun.xml.bind", module = "jaxb-impl")
    }
    implementation("org.camunda.bpm:camunda-engine-plugin-spin")
    implementation("org.camunda.spin:camunda-spin-core")
    implementation("org.camunda.spin:camunda-spin-dataformat-json-jackson")

    // GraalVM Javascript
    implementation("org.graalvm.js:js")
    implementation("org.graalvm.js:js-scriptengine")

    // Submodules
    implementation(project(":dispute-process-definition"))
    implementation(project(":dispute-tasks"))

    // Metrics support
    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-statsd")

    // Tracing support
    implementation("io.opentracing.contrib:opentracing-spring-tracer-configuration-starter")
    implementation("io.opentracing:opentracing-api")
    implementation("io.opentracing.contrib:opentracing-spring-cloud-starter")
    implementation("com.datadoghq:dd-trace-api")
    implementation("com.datadoghq:dd-trace-ot")

    // Testing
    testImplementation("io.opentracing:opentracing-mock")
    testImplementation("org.springframework.security:spring-security-test")
}

// Per profile configuration & dependencies (see buildSrc/main/kotlin/profiles)
val buildProfile: String? by project
apply(plugin = "profile.${buildProfile ?: "default"}")

tasks.getByName<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    systemProperty("polyglot.engine.WarnInterpreterOnly", false)
}
