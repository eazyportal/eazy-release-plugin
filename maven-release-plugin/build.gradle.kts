plugins {
    id("eazy-kotlin-library-conventions") version("+")
}

dependencies {
    implementation("org.apache.maven", "maven-plugin-api", "+")
    implementation("org.apache.maven", "maven-project", "+")
    implementation("org.apache.maven.plugin-tools", "maven-plugin-annotations", "+")

    // Dependencies
    implementation(project(":eazy-release-plugin-core"))
}
