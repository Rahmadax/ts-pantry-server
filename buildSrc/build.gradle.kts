
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.majorVersion
    }
}

dependencies {
    implementation(kotlin(module = "gradle-plugin", version = "1.6.10"))
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.6.10")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.14")
    implementation("io.snyk.gradle.plugin.snykplugin:io.snyk.gradle.plugin.snykplugin.gradle.plugin:0.4")
}

