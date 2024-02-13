package org.eazyportal.plugin.release.gradle.ac.tasks

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.core.version.model.Version
import org.eazyportal.plugin.release.core.version.model.VersionFixtures
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ReleaseTaskAcceptanceTest : EazyBaseTaskAcceptanceTest() {

    private lateinit var gradleRunner: GradleRunner

    @BeforeEach
    fun setUp() {
        GRADLE_PROPERTIES_FILE.writeText("version = ${VersionFixtures.SNAPSHOT_100}")

        gradleRunner = GradleRunner.create()
            .forwardOutput()
            .withProjectDir(PROJECT_DIR)
            .withArguments(EazyReleasePlugin.RELEASE_TASK_NAME)
            .withPluginClasspath()
    }

    @Test
    fun test() {
        // GIVEN
        // WHEN
        val actual = gradleRunner.build()

        // THEN
        assertThat(actual.task(":${EazyReleasePlugin.PREPARE_REPOSITORY_FOR_RELEASE_TASK_NAME}")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        assertThat(actual.task(":${EazyReleasePlugin.SET_RELEASE_VERSION_TASK_NAME}")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        assertThat(actual.task(":${EazyReleasePlugin.FINALIZE_RELEASE_VERSION_TASK_NAME}")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        assertThat(actual.task(":${EazyReleasePlugin.SET_SNAPSHOT_VERSION_TASK_NAME}")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        assertThat(actual.task(":${EazyReleasePlugin.FINALIZE_SNAPSHOT_VERSION_TASK_NAME}")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        assertThat(actual.task(":${EazyReleasePlugin.UPDATE_SCM_TASK_NAME}")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        assertThat(actual.task(":${EazyReleasePlugin.RELEASE_TASK_NAME}")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        assertThat(GRADLE_PROPERTIES_FILE.readText())
            .isEqualTo("version = ${Version(1, 0, 1, Version.DEVELOPMENT_VERSION_SUFFIX)}")
    }

}
