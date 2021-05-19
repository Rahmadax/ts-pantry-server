plugins {
    id("drc-workflow.spring-boot-conventions")
}

// Core dependencies
dependencies {

    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter:7.15.0")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.15.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("io.micrometer:micrometer-jersey2")

    //TODO: add this to a datadog gradle profile
    implementation("io.micrometer:micrometer-registry-datadog")

    implementation(project(":process-definition"))

}

// Per profile configuration & dependencies (see buildSrc/main/kotlin/profiles)
val buildProfile: String? by project
apply(plugin = "profile.${buildProfile ?: "default"}")


