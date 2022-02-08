package org.eazyportal.plugin.release.gradle.tasks

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.mockito.Mockito

internal abstract class EazyReleaseBaseTaskTest<in T: EazyReleaseBaseTask> {

    protected val project: Project = ProjectBuilder.builder()
        .build()
    protected val scmActions: ScmActions = Mockito.mock(ScmActions::class.java)
    protected val scmConfig: ScmConfig = ScmConfig.GIT_FLOW

    protected lateinit var underTest: @UnsafeVariance T

    @Test
    fun test_getGroup() {
        // GIVEN
        // WHEN
        // THEN
        assertThat(underTest.group).isEqualTo(EazyReleaseBaseTask.GROUP)
    }

    @Test
    fun test_setGroup_shouldFail() {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { underTest.group = "newValue" }
            .isInstanceOf(UnsupportedOperationException::class.java)
            .hasMessage("Not allowed to set the group of an ${EazyReleaseBaseTask.GROUP} task.")
    }

}
