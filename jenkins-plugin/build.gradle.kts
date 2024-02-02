import java.net.URI

plugins {
    id("org.jenkins-ci.jpi") version("+")

    id("org.eazyportal.plugin.java-project-convention")
}

jenkinsPlugin {
    jenkinsVersion.set(project.property("jenkinsVersion") as String)

    shortName = "eazy-release"
    displayName = "Eazy Release Plugin"
    gitHub.set(URI("https://github.com/eazyportal/eazyrelease-plugin"))

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
    implementation(platform("io.jenkins.tools.bom:bom-${project.property("jenkinsVersion")}.x:+"))

    compileOnly("org.jenkins-ci.plugins.workflow", "workflow-step-api")
    compileOnly("org.jenkins-ci.plugins.workflow", "workflow-job")

    constraints {
        configurations.all {
            resolutionStrategy.eachDependency {
                if ((requested.group == "org.checkerframework") && (requested.name == "checker-qual")) {
                    useVersion("3.33.0")
                    because("fix: missing dependency version from jenkins/guava")
                } else if ((requested.group == "com.google.j2objc") && (requested.name == "j2objc-annotations")) {
                    useVersion("2.8")
                    because("fix: missing dependency version from jenkins/guava")
                }
            }
        }
    }

    // Test
    testImplementation("org.jenkinsci.plugins", "pipeline-model-definition")

    // Jenkins server dependencies
    jenkinsServer("org.jenkins-ci.plugins.workflow", "workflow-basic-steps")
    jenkinsServer("org.jenkins-ci.plugins.workflow", "workflow-cps")
    jenkinsServer("org.jenkins-ci.plugins.workflow", "workflow-durable-task-step")
    jenkinsServer("org.jenkins-ci.plugins.workflow", "workflow-multibranch")
    jenkinsServer("org.jenkins-ci.plugins.workflow", "workflow-job")
}
