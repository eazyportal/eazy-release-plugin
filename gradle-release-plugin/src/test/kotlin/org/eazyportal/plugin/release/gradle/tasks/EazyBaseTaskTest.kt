package org.eazyportal.plugin.release.gradle.tasks

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EazyBaseTaskTest {

    private val project = ProjectBuilder.builder()
        .build()

    private lateinit var underTest: EazyBaseTask

    @BeforeEach
    fun setUp() {
        underTest = project.tasks.create(EazyReleasePlugin.UPDATE_SCM_TASK_NAME, UpdateScmTask::class.java)
    }

    @Test
    fun test_getGroup() {
        // GIVEN
        // WHEN
        // THEN
        assertThat(underTest.group).isEqualTo("eazy")
    }

    @Test
    fun test_setGroup_shouldFail() {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { underTest.group = "newValue" }
            .isInstanceOf(UnsupportedOperationException::class.java)
            .hasMessage("Not allowed to set the group of EazyTasks.")
    }

}
