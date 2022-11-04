plugins {
    id("org.eazyportal.plugin.kotlin-library-convention")

    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("gradle-plugin") {
            id = group.toString()
            implementationClass = "org.eazyportal.plugin.release.gradle.EazyReleasePlugin"
        }
    }
}

// https://docs.gradle.org/current/userguide/cross_project_publications.html#sec:simple-sharing-artifacts-between-projects
val consumableConfiguration: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
}
artifacts {
    add("consumableConfiguration", tasks.jar)
}

tasks {
    findByName("publishMavenPublicationToGithubRepository")?.enabled = false
    findByName("publishMavenPublicationToMavenLocalRepository")?.enabled = false

    test {
        testLogging {
            outputs.upToDateWhen {
                false
            }

            showStandardStreams = true
        }
    }
}

dependencies {
    implementation(project(":core"))

    testImplementation(testFixtures(project(":core")))
}
