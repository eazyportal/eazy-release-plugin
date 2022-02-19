plugins {
    id("org.eazyportal.plugin.kotlin-project-convention")

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
