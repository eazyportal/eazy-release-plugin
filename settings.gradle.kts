rootProject.name = "eazyrelease-plugin"

pluginManagement {
    plugins {
        id("org.eazyportal.plugin.kotlin-project-convention") version(extra["eazyPluginConventionVersion"] as String)
    }

    repositories {
        gradlePluginPortal()

        maven {
            name = "github"
            url = uri("${extra["githubUrl"] as String}/*")

            credentials {
                password = extra["githubPassword"] as String
                username = extra["githubUsername"] as String
            }
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
