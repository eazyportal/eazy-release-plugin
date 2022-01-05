plugins {
    id("kotlin-conventions")
    `java-gradle-plugin`
}

tasks {
    jar {
        enabled = false
    }
}

dependencies {
    implementation(project(":eazyGradleReleasePlugin"))
}
