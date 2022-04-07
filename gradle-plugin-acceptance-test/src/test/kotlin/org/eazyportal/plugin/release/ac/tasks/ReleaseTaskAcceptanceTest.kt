package org.eazyportal.plugin.release.ac.tasks

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.core.version.model.Version
import org.eazyportal.plugin.release.core.version.model.VersionFixtures
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin.Companion.RELEASE_TASK_NAME
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin.Companion.SET_RELEASE_VERSION_TASK_NAME
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin.Companion.SET_SNAPSHOT_VERSION_TASK_NAME
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin.Companion.UPDATE_SCM_TASK_NAME
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ReleaseTaskAcceptanceTest : EazyBaseTaskAcceptanceTest() {

    private lateinit var gradleRunner: GradleRunner

    @BeforeEach
    fun setUp() {
        GRADLE_PROPERTIES_FILE.writeText("version = ${VersionFixtures.RELEASE_100}")

        gradleRunner = GradleRunner.create()
            .withProjectDir(PROJECT_DIR)
            .withArguments(RELEASE_TASK_NAME)
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

        assertThat(actual.task(":$SET_RELEASE_VERSION_TASK_NAME")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        assertThat(actual.task(":$UPDATE_SCM_TASK_NAME")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        assertThat(actual.task(":$RELEASE_TASK_NAME")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        assertThat(GRADLE_PROPERTIES_FILE.readText())
            .isEqualTo("version = ${Version(1, 1, 1, Version.DEVELOPMENT_VERSION_SUFFIX)}")
    }

}
