plugins {
    id("org.eazyportal.plugin.kotlin-library-convention")
}

dependencies {
    implementation("org.apache.maven", "maven-plugin-api", "+")
    implementation("org.apache.maven", "maven-project", "+")
    implementation("org.apache.maven.plugin-tools", "maven-plugin-annotations", "+")

    // Dependencies
    implementation(project(":core"))
}
