package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.UpdateScmAction
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

internal class UpdateScmTaskTest {

    companion object {
        private const val RELEASE_BRANCH = "release-branch"
        private const val REMOTE = "remote-repository"
    }

    private val project = ProjectBuilder.builder()
        .build()

    @Mock
    private lateinit var updateScmAction: UpdateScmAction

    private lateinit var underTest: UpdateScmTask

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(EazyReleasePlugin.UPDATE_SCM_TASK_NAME, UpdateScmTask::class.java, updateScmAction).apply {
            releaseBranch.set(RELEASE_BRANCH)
            remote.set(REMOTE)
        }
    }

    @Test
    fun test_run() {
        // GIVEN
        // WHEN
        // THEN
        underTest.run()

        verify(updateScmAction).releaseBranch = RELEASE_BRANCH
        verify(updateScmAction).remote = REMOTE
        verify(updateScmAction).execute(project.rootDir)
        verifyNoMoreInteractions(updateScmAction)
    }

}
