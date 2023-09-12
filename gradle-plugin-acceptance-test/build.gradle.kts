plugins {
    `java-gradle-plugin`

    id("org.eazyportal.plugin.kotlin-project-convention")
}

tasks {
    jar {
        enabled = false
    }

    test {
        rootProject.allprojects.forEach {
            if (it != project) {
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
    implementation(project(":gradle-plugin"))

    testImplementation(testFixtures(project(":core")))
}
