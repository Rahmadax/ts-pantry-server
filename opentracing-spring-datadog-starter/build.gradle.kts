plugins {
    id("drc-workflow.kotlin-conventions")
}

// Core dependencies
dependencies {

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-autoconfigure")

    implementation("io.opentracing.contrib:opentracing-spring-tracer-configuration-starter")
    implementation("io.opentracing:opentracing-api")

    //TODO: Exclude all the dependencies that we are not using.
    implementation("io.opentracing.contrib:opentracing-spring-cloud-starter")

    implementation("com.datadoghq:dd-trace-api")
    implementation("com.datadoghq:dd-trace-ot")

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