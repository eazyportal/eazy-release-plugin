plugins {
    id("org.eazyportal.plugin.kotlin-project-convention")
}

tasks.jar {
    isEnabled = false
}

subprojects {
    afterEvaluate {
        dependencies {
            testImplementation("org.assertj", "assertj-core", "+")
            testImplementation("org.junit.jupiter", "junit-jupiter", "+")
            testImplementation("org.mockito", "mockito-inline", "+")
            testImplementation("org.mockito.kotlin", "mockito-kotlin", "+")
        }
    }
}
