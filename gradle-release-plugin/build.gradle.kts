plugins {
    id("eazy-kotlin-library-conventions")
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("eazyGradleReleasePlugin") {
            id = "$group.release"
            implementationClass = "org.eazyportal.plugin.release.gradle.EazyReleasePlugin"
        }
    }
}

dependencies {
    api(project(":eazy-release-plugin-core"))
}
