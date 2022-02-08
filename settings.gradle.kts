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
include("gradle-plugin")
include("gradle-plugin-acceptance-test")

// Jenkins
include("jenkins-plugin")

// Maven
include("maven-plugin")
