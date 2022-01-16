plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenLocal()
}

dependencies {
    implementation("org.eazyportal.plugin.convention", "eazy-project-convention-gradle-plugin", "+")
}
