package org.eazyportal.plugin.release.gradle.ac.tasks

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PrepareRepositoryForReleaseTaskAcceptanceTest : EazyBaseTaskAcceptanceTest() {

    private lateinit var gradleRunner: GradleRunner

    @BeforeEach
    fun setUp() {
        gradleRunner = GradleRunner.create()
            .forwardOutput()
            .withProjectDir(PROJECT_DIR)
            .withArguments(EazyReleasePlugin.PREPARE_REPOSITORY_FOR_RELEASE_TASK_NAME)
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
    }

}
