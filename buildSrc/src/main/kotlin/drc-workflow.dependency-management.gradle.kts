plugins {
    id("io.spring.dependency-management")
    id("io.snyk.gradle.plugin.snykplugin")
}

configure<io.snyk.gradle.plugin.SnykExtension> {
    val snykApiKey = System.getenv("SNYK_TOKEN") ?: project.properties.getOrDefault("snyk_token", null) as String? ?: ""
    setApi(snykApiKey)
    setArguments("--all-sub-projects --policy-path=.snyk")
}

// Version management
dependencyManagement {

    imports {
        // Camunda Dependencies
        mavenBom("org.camunda.bpm:camunda-bom:7.16.0")

        // Spring dependencies
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }

    configurations.all {
        exclude(group = "org.apache.logging.log4j", module = "log4j-api")
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
    }

    dependencies {


        // GraalVM JS Engine
        dependency("org.graalvm.js:js:22.0.0")
        dependency("org.graalvm.js:js-scriptengine:22.0.0")

        // Security dependencies
        dependency("com.depop:depop-jwt_2.13:0.0.23")
        dependency("com.okta.spring:okta-spring-security-oauth2:1.1.0")
        dependency("org.springframework.security:spring-security-web:5.6.9")
        dependency("org.springframework.security:spring-security-oauth2-client:5.6.9")

        // Opentracing dependencies
        dependency("io.opentracing.contrib:opentracing-spring-cloud-starter:0.5.9")
        dependency("io.opentracing.contrib:opentracing-spring-tracer-configuration-starter:0.4.0")
        dependency("io.opentracing:opentracing-api:0.33.0")
        dependency("io.opentracing:opentracing-mock:0.33.0")

        // Datadog dependencies
        dependency("com.datadoghq:dd-trace-api:0.110.0")
        dependency("com.datadoghq:dd-trace-ot:0.110.0")

        // General dependencies
        dependency("io.github.microutils:kotlin-logging-jvm:2.0.11")

        // Upgrading version of jnr-posix due to snyk DOS vulnerability report.
        dependency("com.github.jnr:jnr-posix:3.1.8")

        // Upgrading version of snakeyaml due to snyk DOS vulnerability report.
        dependency("org.yaml:snakeyaml:1.31")

        // SNYK-JAVA-COMH2DATABASE-2331071 | SNYK-JAVA-COMH2DATABASE-1769238
        dependency("com.h2database:h2:2.1.214")

        // SNYK-JAVA-COMSQUAREUPOKHTTP3-2958044
        dependency("com.squareup.okhttp3:okhttp:4.9.2")

        // SNYK-JAVA-COMMONSFILEUPLOAD-3326457
        dependency("commons-fileupload:commons-fileupload:1.5")

    }

}
