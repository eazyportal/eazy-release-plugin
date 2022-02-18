plugins {
    id("org.eazyportal.plugin.java-project-convention")

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
    implementation(project(":core")) {
        exclude("org.slf4j", "slf4j-api") // Jenkins uses an old version
    }
    // https://docs.gradle.org/current/userguide/cross_project_publications.html#sec:simple-sharing-artifacts-between-projects
    implementation(project(mapOf("path" to ":gradle-plugin", "configuration" to "consumableConfiguration")))

    // Jenkins BOM
    implementation(platform("io.jenkins.tools.bom:bom-2.319.x:+"))

    compileOnly("org.jenkins-ci.plugins", "plugin", "4.12")
    compileOnly("org.jenkins-ci.plugins.workflow", "workflow-step-api")
    compileOnly("org.jenkins-ci.plugins.workflow", "workflow-job")

    // Test
    testImplementation("org.jenkinsci.plugins", "pipeline-model-definition")

    // Jenkins server dependencies
    jenkinsServer("org.jenkins-ci.plugins.workflow", "workflow-basic-steps")
    jenkinsServer("org.jenkins-ci.plugins.workflow", "workflow-cps")
    jenkinsServer("org.jenkins-ci.plugins.workflow", "workflow-durable-task-step")
    jenkinsServer("org.jenkins-ci.plugins.workflow", "workflow-multibranch")
    jenkinsServer("org.jenkins-ci.plugins.workflow", "workflow-job")
}
