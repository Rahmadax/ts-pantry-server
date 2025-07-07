plugins {
    id("drc-workflow.spring-boot-lib-conventions")
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.8"
}

group = "com.depop.cx"
version = System.getenv("PUBLISH_VERSION") ?: "local"

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["shadow"])

            groupId = project.group as String
            artifactId = "dispute-tasks"
            version = project.version as String
        }
    }

    repositories {
        maven {
            name = "Artifactory"
            url = uri(System.getenv("ARTIFACTORY_URL") ?: "https://depop.jfrog.io/artifactory/depop-snapshot-local/")

            credentials {
                username = System.getenv("JFROG_API_USERNAME")
                password = System.getenv("JFROG_API_KEY")
            }
        }
    }
}

// Core dependencies
dependencies {

    // General
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.oshai:kotlin-logging-jvm")

    // Spring boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // General
    implementation("org.camunda.bpm:camunda-engine")
    implementation("org.camunda.spin:camunda-spin-core")
    implementation("org.xmlunit:xmlunit-core")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // Metrics support
    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-statsd")
}