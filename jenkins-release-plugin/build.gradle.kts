import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("eazy-kotlin-library-conventions") version("+")
    id("org.jenkins-ci.jpi") version("+")
}

jenkinsPlugin {
    jenkinsVersion.set("2.319")
    displayName = "Eazy Release Plugin"

    configurePublishing = false
    pluginFirstClassLoader = true
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType(KotlinCompile::class.java).all {
    dependsOn("localizer")

    kotlinOptions {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"

        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":eazy-release-gradle-plugin"))

    // Jenkins BOM
    implementation(platform("io.jenkins.tools.bom:bom-2.319.x:+"))

    compileOnly("org.jenkins-ci.plugins", "plugin", "4.12")
    compileOnly("org.jenkins-ci.plugins.workflow", "workflow-step-api")
    compileOnly("org.jenkins-ci.plugins.workflow", "workflow-job")

    // Test service dependencies
    testImplementation("org.jenkinsci.plugins", "pipeline-model-definition")
}
