package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SetSnapshotVersionTaskTest {

    private val project = ProjectBuilder.builder()
        .build()

    private lateinit var underTest: SetSnapshotVersionTask

    @BeforeEach
    fun setUp() {
        underTest = project.tasks.create(EazyReleasePlugin.UPDATE_SCM_TASK_NAME, SetSnapshotVersionTask::class.java)
    }

    @Test
    fun test_run() {
        // GIVEN
        // WHEN
        // THEN
        underTest.run()
    }

}
