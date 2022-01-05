plugins {
    id("kotlin-library-conventions")
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

tasks {
    jar {
        manifest {
            attributes["Implementation-Version"] = project.version
        }
    }
}

dependencies {
    api(project(":eazy-release-plugin-core"))
}
