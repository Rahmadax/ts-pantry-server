plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(kotlin(module = "gradle-plugin", version = "1.9.24"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.9.24")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.6")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.18")
}

