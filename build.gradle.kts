group = "com.depop"
version = "0.0.1"

plugins {
    id("drc.spring-boot-conventions")
}

dependencies {
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter:7.15.0")
    runtimeOnly("com.h2database:h2")
}

sourceSets {
    val main by getting
    main.resources.srcDirs("src/main/processes")
}
