plugins {
    `java`
    `maven-publish`
    id("org.eazyportal.plugin.release-gradle-plugin")
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
