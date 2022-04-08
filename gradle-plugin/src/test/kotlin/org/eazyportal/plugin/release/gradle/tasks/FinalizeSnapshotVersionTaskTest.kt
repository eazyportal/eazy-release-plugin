package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.ProjectDescriptorFactory
import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.action.FinalizeSnapshotVersionActionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class FinalizeSnapshotVersionTaskTest : EazyReleaseBaseTaskTest< FinalizeSnapshotVersionTask>() {

    @Mock
    private lateinit var finalizeSnapshotVersionActionFactory: FinalizeSnapshotVersionActionFactory
    @Mock
    private lateinit var projectDescriptorFactory: ProjectDescriptorFactory

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.FINALIZE_SNAPSHOT_VERSION_TASK_NAME,
            FinalizeSnapshotVersionTask::class.java,
            projectDescriptorFactory,
            finalizeSnapshotVersionActionFactory
        )
    }

    @Test
    fun test_run() {
        // GIVEN
        val finalizeSnapshotVersionAction: FinalizeSnapshotVersionAction = mock()
        val projectDescriptor: ProjectDescriptor = mock()

        // WHEN
        whenever(projectDescriptorFactory.create(extension.projectActionsFactory, extension.scmActions, project.projectDir))
            .thenReturn(projectDescriptor)
        whenever(finalizeSnapshotVersionActionFactory.create(extension)).thenReturn(finalizeSnapshotVersionAction)

        // THEN
        underTest.run()

        verifyNoInteractions(projectDescriptor)

        verify(projectDescriptorFactory).create(extension.projectActionsFactory, extension.scmActions, project.projectDir)

        verify(finalizeSnapshotVersionActionFactory).create(extension)
        verify(finalizeSnapshotVersionAction).execute(projectDescriptor)

        verifyNoMoreInteractions(finalizeSnapshotVersionAction, finalizeSnapshotVersionActionFactory)
    }

}
