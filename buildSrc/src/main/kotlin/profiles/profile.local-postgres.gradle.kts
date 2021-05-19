import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("drc-workflow.kotlin-conventions") apply false
}

dependencies {
    runtimeOnly("org.postgresql:postgresql")
}

tasks.named<BootRun>("bootRun") {
    args("--spring.profiles.active=local,postgres,datadog")
}
