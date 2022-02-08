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
    implementation(project(":core"))
    implementation(project(":gradle-plugin"))
}
