plugins {
    id("org.springframework.boot")
    id("drc.kotlin-conventions")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

dependencies {

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Common Boot starters
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

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
