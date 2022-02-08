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

// Core
include("core")

// Gradle
include("gradle-release-plugin")

include("gradle-release-plugin-acceptance-test")

// Jenkins
include("jenkins-release-plugin")

// Maven
include("maven-release-plugin")
