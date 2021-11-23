import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("jacoco")
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    test {
        useJUnitPlatform()

        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
        dependsOn(test)
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Test dependencies
    testImplementation("org.assertj", "assertj-core", "+")
    testImplementation("org.junit.jupiter", "junit-jupiter", "+")
    testImplementation("org.mockito", "mockito-inline", "+")
    testImplementation("org.mockito.kotlin", "mockito-kotlin", "+")
}
