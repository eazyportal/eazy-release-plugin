plugins {
    id("eazy-kotlin-library-conventions")

    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("gradle-plugin") {
            id = "$group-$name"
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

dependencies {
    implementation(project(":core"))
}
