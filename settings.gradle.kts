rootProject.name = "user-default-disputeworkflow-internal"
include("dispute-workflow", "dispute-process-definition", "opentracing-spring-datadog-starter")

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://depop.jfrog.io/depop/depop-clean-scala-maven")
            name = "depopJFrog"
            credentials(PasswordCredentials::class)
        }
    }
}
