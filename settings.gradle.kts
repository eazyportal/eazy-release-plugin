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
include("eazy-release-plugin-core")
project(":eazy-release-plugin-core").projectDir = file("core")

// Gradle
include("eazy-release-gradle-plugin")
project(":eazy-release-gradle-plugin").projectDir = file("gradle-release-plugin")

include("gradle-release-plugin-acceptance-test")

// Jenkins
include("eazy-release-jenkins-plugin")
project(":eazy-release-jenkins-plugin").projectDir = file("jenkins-release-plugin")

// Maven
include("eazy-release-maven-plugin")
project(":eazy-release-maven-plugin").projectDir = file("maven-release-plugin")
