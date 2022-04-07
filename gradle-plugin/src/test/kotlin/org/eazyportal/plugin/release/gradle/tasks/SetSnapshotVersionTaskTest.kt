package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.ProjectDescriptorFactory
import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction
import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.action.FinalizeSnapshotVersionActionFactory
import org.eazyportal.plugin.release.gradle.action.SetSnapshotVersionActionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class SetSnapshotVersionTaskTest : EazyReleaseBaseTaskTest<SetSnapshotVersionTask>() {

    @Mock
    private lateinit var finalizeSnapshotVersionActionFactory: FinalizeSnapshotVersionActionFactory
    @Mock
    private lateinit var projectDescriptorFactory: ProjectDescriptorFactory
    @Mock
    private lateinit var setSnapshotVersionActionFactory: SetSnapshotVersionActionFactory

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.UPDATE_SCM_TASK_NAME,
            SetSnapshotVersionTask::class.java,
            projectDescriptorFactory ,
            setSnapshotVersionActionFactory,
            finalizeSnapshotVersionActionFactory
        )
    }

    @Test
    fun test_run() {
        // GIVEN
        val finalizeSnapshotVersionAction: FinalizeSnapshotVersionAction = mock()
        val projectDescriptor: ProjectDescriptor = mock()
        val setSnapshotVersionAction: SetSnapshotVersionAction = mock()

        // WHEN
        whenever(projectDescriptorFactory.create(extension.projectActionsFactory, extension.scmActions, project.projectDir))
            .thenReturn(projectDescriptor)
        whenever(setSnapshotVersionActionFactory.create(extension)).thenReturn(setSnapshotVersionAction)
        whenever(finalizeSnapshotVersionActionFactory.create(extension)).thenReturn(finalizeSnapshotVersionAction)

        // THEN
        underTest.run()

        verifyNoInteractions(projectDescriptor)

        verify(projectDescriptorFactory).create(extension.projectActionsFactory, extension.scmActions, project.projectDir)

        verify(setSnapshotVersionActionFactory).create(extension)
        verify(setSnapshotVersionAction).execute(projectDescriptor)

        verify(finalizeSnapshotVersionActionFactory).create(extension)
        verify(finalizeSnapshotVersionAction).execute(projectDescriptor)

        verifyNoMoreInteractions(finalizeSnapshotVersionAction, finalizeSnapshotVersionActionFactory, setSnapshotVersionAction, setSnapshotVersionActionFactory)
    }

}
