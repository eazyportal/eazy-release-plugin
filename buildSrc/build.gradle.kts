plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenLocal()
}

dependencies {
    implementation("org.eazyportal.plugin", "eazy-gradle-project-convention-plugin", "+")
}
