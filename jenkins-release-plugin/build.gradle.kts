plugins {
    id("eazy-java-conventions") version("+")

    id("org.jenkins-ci.jpi") version("+")
}

jenkinsPlugin {
    jenkinsVersion.set("2.319")
    displayName = "Eazy Release Plugin"

    configurePublishing = false
    pluginFirstClassLoader = true

    developers {
        developer {
            id.set("Yg0R2")
            name.set("Tibor Kovacs")
            email.set("tibor.kovacs@eazyportal.org")
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// fix "Implicit dependencies between tasks" warning
tasks.generateLicenseInfo {
    dependsOn(tasks.jar)
}

dependencies {
    implementation(project(":gradle-release-plugin"))

    // Jenkins BOM
    implementation(platform("io.jenkins.tools.bom:bom-2.319.x:+"))

    compileOnly("org.jenkins-ci.plugins", "plugin", "4.12")
    compileOnly("org.jenkins-ci.plugins.workflow", "workflow-step-api")
    compileOnly("org.jenkins-ci.plugins.workflow", "workflow-job")

    // Test service dependencies
    testImplementation("org.jenkinsci.plugins", "pipeline-model-definition")
}
