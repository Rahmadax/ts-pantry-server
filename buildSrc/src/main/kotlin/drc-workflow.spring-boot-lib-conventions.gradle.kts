plugins {
    id("drc-workflow.kotlin-conventions")
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlin.plugin.spring")
}

dependencies {

    compileOnly("org.springframework.boot:spring-boot")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

configurations.compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
}