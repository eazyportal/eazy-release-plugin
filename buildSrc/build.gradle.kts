plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()

    maven {
        name = "github"
        url = uri("https://maven.pkg.github.com/eazyportal/*")
        credentials(PasswordCredentials::class)
    }
}

dependencies {
    implementation("org.eazyportal.plugin.convention", "eazy-project-convention-gradle-plugin", "+")
}
