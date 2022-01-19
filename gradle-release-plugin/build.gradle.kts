plugins {
    id("eazy-kotlin-library-conventions") version("+")
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("eazy-release-gradle-plugin") {
            id = "$group"
            implementationClass = "org.eazyportal.plugin.release.gradle.EazyReleasePlugin"
        }
    }
}

dependencies {
    api(project(":eazy-release-plugin-core"))
}
