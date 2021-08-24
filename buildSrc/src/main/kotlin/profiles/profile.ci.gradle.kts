import gradle.kotlin.dsl.accessors._a617796451ec58458a40bef9441cce2c.testImplementation

plugins {
    id("drc-workflow.kotlin-conventions") apply false
}

dependencies {
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("com.h2database:h2")
}