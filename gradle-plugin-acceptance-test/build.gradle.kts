plugins {
    id("org.eazyportal.plugin.kotlin-project-convention")

    `java-gradle-plugin`
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
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":gradle-plugin"))

    testImplementation(testFixtures(project(":core")))
}
