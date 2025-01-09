plugins {
    id("org.springframework.boot")
    id("drc-workflow.kotlin-conventions")
    id("org.jetbrains.kotlin.plugin.spring")
}

dependencies {

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Common Boot starters
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

configurations.compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
}

// Don't include the version in the build archive, so we can reliably find it in Docker/make files etc.
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveVersion.set("")
}

// Don't output the plain (thin) jar
tasks.getByName<Jar>("jar") {
    enabled = false
}
