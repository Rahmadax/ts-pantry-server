plugins {
    id("drc-workflow.spring-boot-lib-conventions")
}

// Core dependencies
dependencies {

    // General
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.microutils:kotlin-logging-jvm")

    // Spring boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-webflux") {
      exclude("io.netty:netty-codec-http")
      exclude("io.netty:netty-codec-http2")
    }

    implementation("io.netty:netty-codec-http:4.1.113.Final")
    implementation("io.netty:netty-codec-http2:4.1.113.Final")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // General
    implementation("org.camunda.bpm:camunda-engine")
    implementation("org.camunda.spin:camunda-spin-core")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

