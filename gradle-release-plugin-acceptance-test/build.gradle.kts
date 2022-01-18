plugins {
    id("eazy-kotlin-conventions")
    `java-gradle-plugin`
}

tasks {
    jar {
        enabled = false
    }
}

dependencies {
    implementation(project(":eazy-release-gradle-plugin"))
}
