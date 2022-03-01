import org.eazyportal.plugin.release.ac.project.StubProjectActionsFactory

plugins {
    `java`
    `maven-publish`
    id("org.eazyportal.plugin.release-gradle-plugin")
}

eazyRelease {
    projectActionsFactory = StubProjectActionsFactory()
}

publishing {
    repositories {
        mavenLocal()
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(project.components["java"])
        }
    }
}

project(":submodule-project").tasks {
    create("build") {
        doLast {
            println("Hello from custom build task!")
        }
    }

    create("publish") {
        doLast {
            println("Hello from custom publish task!")
        }
    }
}
