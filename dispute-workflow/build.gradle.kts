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
    implementation("org.springframework.boot:spring-boot-starter-jersey") {
      exclude("org.apache.tomcat.embed:tomcat-embed-core")
    }
    // Snyk https://security.snyk.io/vuln/SNYK-JAVA-ORGAPACHETOMCATEMBED-6092281
    implementation("org.apache.tomcat.embed:tomcat-embed-core:9.0.88")
    implementation("org.apache.tomcat.embed:tomcat-embed-websocket:9.0.88")
    
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Session
    implementation("org.springframework.session:spring-session-jdbc")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // Camunda
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-webapp")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-test")
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
    implementation("io.opentracing.contrib:opentracing-spring-cloud-starter") //TODO: Exclude all the dependencies that we are not using.
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
