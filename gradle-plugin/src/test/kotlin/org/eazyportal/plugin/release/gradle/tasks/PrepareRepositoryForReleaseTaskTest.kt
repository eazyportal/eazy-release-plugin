package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.PrepareRepositoryForReleaseAction
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.action.PrepareRepositoryForReleaseActionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class PrepareRepositoryForReleaseTaskTest : EazyReleaseBaseTaskTest<PrepareRepositoryForReleaseTask>() {

    @Mock
    private lateinit var prepareRepositoryForReleaseActionFactory: PrepareRepositoryForReleaseActionFactory

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.PREPARE_REPOSITORY_FOR_RELEASE_TASK_NAME,
            PrepareRepositoryForReleaseTask::class.java,
            prepareRepositoryForReleaseActionFactory
        )
    }

    @Test
    fun test_run() {
        // GIVEN
        val prepareRepositoryForReleaseAction: PrepareRepositoryForReleaseAction = mock()

        // WHEN
        whenever(prepareRepositoryForReleaseActionFactory.create(extension)).thenReturn(prepareRepositoryForReleaseAction)

        // THEN
        underTest.run()

        verify(prepareRepositoryForReleaseActionFactory).create(extension)
        verify(prepareRepositoryForReleaseAction).execute(project.projectDir)

        verifyNoMoreInteractions(prepareRepositoryForReleaseAction, prepareRepositoryForReleaseActionFactory,)
    }

}
