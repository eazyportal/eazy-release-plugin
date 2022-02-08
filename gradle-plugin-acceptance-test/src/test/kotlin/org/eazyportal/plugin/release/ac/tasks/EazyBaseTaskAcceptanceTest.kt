package org.eazyportal.plugin.release.ac.tasks

import org.eazyportal.plugin.release.ac.stubs.StubScmActions
import org.eazyportal.plugin.release.gradle.project.GradleProjectActions
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.nio.file.Files

internal abstract class EazyBaseTaskAcceptanceTest {

    protected val workingDir: File = Files.createTempDirectory("").toFile()
    protected val buildFile = workingDir.resolve("build.gradle.kts")
    protected val gradlePropertiesFile = workingDir.resolve(GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME)

    @BeforeEach
    fun baseSetUp() {
        buildFile.writeText("""
            plugins {
                `java`
                id("org.eazyportal.plugin.release-gradle-plugin")
            }

            release {
                scmActions = ${StubScmActions::class.java.name}()
            }
        """.trimIndent())
    }

}
