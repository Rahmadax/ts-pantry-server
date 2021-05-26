plugins {
    id("drc-workflow.kotlin-conventions")
}

// Core dependencies
dependencies {

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-autoconfigure")

    implementation("io.opentracing.contrib:opentracing-spring-tracer-configuration-starter")
    implementation("io.opentracing:opentracing-api")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-logging")
    testImplementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("io.opentracing:opentracing-mock")

}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}