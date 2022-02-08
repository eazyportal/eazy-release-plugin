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
    implementation(project(":gradle-release-plugin"))
}
