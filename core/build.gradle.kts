plugins {
    `java-test-fixtures`

    id("org.eazyportal.plugin.kotlin-library-convention")
}

dependencies {
    implementation("org.eclipse.jgit", "org.eclipse.jgit", "+")

    implementation("org.slf4j", "slf4j-api", "+")
}
