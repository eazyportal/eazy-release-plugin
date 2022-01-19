plugins {
    id("eazy-kotlin-conventions") version("+")
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
