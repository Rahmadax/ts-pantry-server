plugins {
    id("drc-workflow.spring-boot-lib-conventions")
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.8"
}

group = "com.depop.cx"

/*
`dispute-definitions` is the only consumer of this publication
it uses the artifact in workflow tests

`PUBLISH_VERSION` will be set (by `scripts/artifactory_publish.sh`) for PR snapshot publications only

master publications will use the default value below
feel free to bump it as appropriate. `dispute-definitions` should always depend on the latest version
so ensure you bump that too if needed!
 */
version = System.getenv("PUBLISH_VERSION") ?: "1.0.0"

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