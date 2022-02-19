package org.eazyportal.plugin.release.ac.tasks

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.core.version.model.Version
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin.Companion.SET_SNAPSHOT_VERSION_TASK_NAME
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SetSnapshotVersionTaskAcceptanceTest : EazyBaseTaskAcceptanceTest() {

    private lateinit var gradleRunner: GradleRunner

    @BeforeEach
    fun setUp() {
        gradlePropertiesFile.writeText("version = ${Version(1, 0, 0)}")

        gradleRunner = GradleRunner.create()
            .withProjectDir(workingDir)
            .withArguments(SET_SNAPSHOT_VERSION_TASK_NAME)
            .withPluginClasspath()
    }

    @Test
    fun test() {
        // GIVEN
        // WHEN
        val actual = gradleRunner.build()

        // THEN
        assertThat(actual.task(":$SET_SNAPSHOT_VERSION_TASK_NAME")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        assertThat(gradlePropertiesFile.readText())
            .isEqualTo("version = ${Version(1, 0, 1, Version.DEVELOPMENT_VERSION_SUFFIX)}")
    }

}
