package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.PrepareRepositoryForReleaseAction
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.action.ReleaseActionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.File

internal class PrepareRepositoryForReleaseTaskTest : EazyReleaseBaseTaskTest<PrepareRepositoryForReleaseTask>() {

    @Mock
    private lateinit var releaseActionFactory: ReleaseActionFactory

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.PREPARE_REPOSITORY_FOR_RELEASE_TASK_NAME,
            PrepareRepositoryForReleaseTask::class.java,
            releaseActionFactory
        )
    }

    @Disabled("It is not possible to mock inline functions.")
    @Test
    fun test_run() {
        // GIVEN
        val prepareRepositoryForReleaseAction: PrepareRepositoryForReleaseAction<File> = mock()

        // WHEN
        whenever(releaseActionFactory.create<PrepareRepositoryForReleaseAction<File>>(project))
            .thenReturn(prepareRepositoryForReleaseAction)

        doNothing().whenever(prepareRepositoryForReleaseAction).execute()

        // THEN
        underTest.run()

        verify(releaseActionFactory).create<PrepareRepositoryForReleaseAction<File>>(project)
        verify(prepareRepositoryForReleaseAction).execute()
        verifyNoMoreInteractions(prepareRepositoryForReleaseAction, releaseActionFactory)
    }

}
