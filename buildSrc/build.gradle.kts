plugins {
    id("org.gradle.kotlin.kotlin-dsl") version "2.1.6"
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.cyclonedx.bom:org.cyclonedx.bom.gradle.plugin:1.4.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
}
