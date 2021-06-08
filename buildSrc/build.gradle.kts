plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(kotlin(module = "gradle-plugin", version = "1.5.0"))
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.5.0")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.10.RELEASE")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.4.5")
}

