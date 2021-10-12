plugins {
    id("drc-workflow.kotlin-conventions") apply false
}

dependencies {
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("com.h2database:h2")
}