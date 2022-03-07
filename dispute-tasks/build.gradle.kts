plugins {
    id("drc-workflow.spring-boot-lib-conventions")
}

// Core dependencies
dependencies {

    // General
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.microutils:kotlin-logging-jvm")

    // Spring boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // General
    implementation("org.camunda.bpm:camunda-engine")
    implementation("org.camunda.spin:camunda-spin-core")

}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

