group = "com.depop"
version = "0.0.1"

plugins {
    id("drc-workflow.spring-boot-conventions")
}


// Core dependencies
dependencies {
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter:7.15.0")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.15.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}


// Per profile configuration & dependencies
val buildProfile: String? by project
apply(plugin = "profile.${buildProfile ?: "default"}")


// Process definitions
sourceSets {
    val main by getting
    main.resources.srcDirs("src/main/processes")
}
