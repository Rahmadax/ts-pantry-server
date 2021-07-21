plugins {
    id("io.spring.dependency-management")
    id("io.snyk.gradle.plugin.snykplugin")
}

configure<io.snyk.gradle.plugin.SnykExtension> {
    val snykApiKey = System.getenv("SNYK_TOKEN") ?: project.properties.getOrDefault("snyk_token", null) as String? ?: ""
    setApi(snykApiKey)
    setArguments("--all-sub-projects")
}

// Version management
// NOTE: Spring boot dependency versions are managed by the spring boot BOM
// please see drc-workflow.spring-boot-conventions.gradle.kts
dependencyManagement {

    imports {
        // Camunda dependencies
        mavenBom("org.camunda.bpm:camunda-bom:7.15.0")
    }

    dependencies {

        // Opentracing dependencies
        dependency("io.opentracing.contrib:opentracing-spring-cloud-starter:0.5.9")
        dependency("io.opentracing.contrib:opentracing-spring-tracer-configuration-starter:0.4.0")
        dependency("io.opentracing:opentracing-api:0.33.0")
        dependency("io.opentracing:opentracing-mock:0.33.0")

        // Datadog dependencies
        dependency("com.datadoghq:dd-trace-api:0.80.0")
        dependency("com.datadoghq:dd-trace-ot:0.80.0")

        // Upgrading version of apache commons compress due to snyk DOS vulnerability report.
        dependency("org.apache.commons:commons-compress:1.21")

        // Upgrade version of jersey common due to a snyk info disclosure vulnerability report.
        dependency("org.glassfish.jersey.core:jersey-common:2.34")

    }
}
