plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(kotlin(module = "gradle-plugin", version = "1.4.31"))
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.4.31")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.5.1")
    implementation("io.snyk.gradle.plugin.snykplugin:io.snyk.gradle.plugin.snykplugin.gradle.plugin:0.4")
}

