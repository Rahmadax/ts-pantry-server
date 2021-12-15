rootProject.name = "user-default-dispute-workflow"
include("dispute-workflow", "dispute-process-definition", "dispute-tasks")

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://depop.jfrog.io/depop/depop-clean-scala-maven")
            credentials {
                username = ""
                password = System.getenv("JFROG_API_KEY") ?: settings.extra["jfrog_api_key"] as String?
            }
        }
    }
}