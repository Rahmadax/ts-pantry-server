rootProject.name = "user-default-disputeworkflow-internal"
include("dispute-workflow", "dispute-process-definition")

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://depop.jfrog.io/depop/depop-clean-scala-maven")
            name = "depopJFrog"
            credentials(PasswordCredentials::class)
        }
    }
}
