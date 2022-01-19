rootProject.name = "eazy-release-plugin"

pluginManagement {
    repositories {
        gradlePluginPortal()

        maven {
            val githubUrl: String by settings

            name = "github"

            url = uri("$githubUrl/*")
            credentials(PasswordCredentials::class)
        }
    }
}

include("eazy-release-plugin-core")
project(":eazy-release-plugin-core").projectDir = file("core")

include("eazy-release-gradle-plugin")
project(":eazy-release-gradle-plugin").projectDir = file("gradle-release-plugin")

include("gradle-release-plugin-acceptance-test")

include("eazy-release-maven-plugin")
project(":eazy-release-maven-plugin").projectDir = file("maven-release-plugin")
