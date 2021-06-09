import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("drc-workflow.kotlin-conventions") apply false
}

dependencies {
    runtimeOnly("com.h2database:h2")
}

tasks.named<BootRun>("bootRun") {
    args("--spring.profiles.active=dev-h2")
}
