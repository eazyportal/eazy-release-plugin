package org.eazyportal.plugin.release.gradle

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files

internal class EazyReleasePluginTest {

    private val workingDir = Files.createTempDirectory("").toFile()

    private lateinit var gradleRunner: GradleRunner

    @BeforeEach
    fun setUp() {
        gradleRunner = GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withProjectDir(workingDir)
            .withTestKitDir(workingDir)
    }

    @AfterEach
    fun tearDown() {
        workingDir.deleteOnExit()
    }

    @Test
    fun test_plugin_withGroovySyntax() {
        // GIVEN
        workingDir.resolve("build.gradle")
            .writeText("""
                plugins {
                    id 'java'
                    id 'org.eazyportal.plugin.release'
                }
            """.trimIndent())

        // WHEN
        // THEN
        val actual = gradleRunner.withArguments("tasks")
            .build()

        assertThat(actual.task(":tasks")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(actual.output).contains(
            "Eazy tasks",
            EazyReleasePlugin.RELEASE_TASK_NAME,
            EazyReleasePlugin.SET_RELEASE_VERSION_TASK_NAME,
            EazyReleasePlugin.SET_SNAPSHOT_VERSION_TASK_NAME,
            EazyReleasePlugin.UPDATE_SCM_TASK_NAME
        )
    }

    @Test
    fun test_plugin_withKotlinSyntax() {
        // GIVEN
        workingDir.resolve("build.gradle.kts")
            .writeText("""
                plugins {
                    `kotlin-dsl`
                    id("org.eazyportal.plugin.release")
                }
            """.trimIndent())

        // WHEN
        // THEN
        val actual = gradleRunner.withArguments("tasks")
            .build()

        assertThat(actual.task(":tasks")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(actual.output).contains(
            "Eazy tasks",
            EazyReleasePlugin.RELEASE_TASK_NAME,
            EazyReleasePlugin.SET_RELEASE_VERSION_TASK_NAME,
            EazyReleasePlugin.SET_SNAPSHOT_VERSION_TASK_NAME,
            EazyReleasePlugin.UPDATE_SCM_TASK_NAME
        )
    }

}
