package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.SetSnapshotVersionAction
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.SetSnapshotVersionActionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class SetSnapshotVersionTaskTest : EazyReleaseBaseTaskTest<SetSnapshotVersionTask>() {

    @Mock
    private lateinit var setSnapshotVersionActionFactory: SetSnapshotVersionActionFactory

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(EazyReleasePlugin.UPDATE_SCM_TASK_NAME, SetSnapshotVersionTask::class.java, setSnapshotVersionActionFactory)
    }

    @Test
    fun test_run() {
        // GIVEN
        val setSnapshotVersionAction = mock<SetSnapshotVersionAction>()

        // WHEN
        whenever(setSnapshotVersionActionFactory.create(projectActionsFactory, extension)).thenReturn(setSnapshotVersionAction)

        // THEN
        underTest.run()

        verify(setSnapshotVersionActionFactory).create(projectActionsFactory, extension)
        verify(setSnapshotVersionAction).execute(project.rootDir)
        verifyNoMoreInteractions(setSnapshotVersionAction, setSnapshotVersionActionFactory)
    }

}
