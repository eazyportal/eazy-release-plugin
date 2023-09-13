plugins {
    id("org.eazyportal.plugin.kotlin-project-convention")
}

tasks {
    jar {
        enabled = false
    }

    test {
        rootProject.allprojects.forEach {
            if ((it != project) && it.name != "gradle-plugin-acceptance-test") {
                mustRunAfter(it.tasks.test)
            }
        }

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

    implementation(testFixtures(project(":core")))

    implementation("org.assertj", "assertj-core", "+")
    implementation("org.junit.jupiter", "junit-jupiter", "+")
    implementation("org.mockito", "mockito-inline", "+")
    implementation("org.mockito.kotlin", "mockito-kotlin", "+")
}
