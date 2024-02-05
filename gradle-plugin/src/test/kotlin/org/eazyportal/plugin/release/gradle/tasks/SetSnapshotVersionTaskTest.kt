package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.FixtureValues.ACTION_CONTEXT
import org.eazyportal.plugin.release.core.ProjectDescriptorFactory
import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.action.ActionContextFactory
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
    private lateinit var actionContextFactory: ActionContextFactory
    @Mock
    private lateinit var projectDescriptorFactory: ProjectDescriptorFactory
    @Mock
    private lateinit var setSnapshotVersionActionFactory: SetSnapshotVersionActionFactory

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.SET_SNAPSHOT_VERSION_TASK_NAME,
            SetSnapshotVersionTask::class.java,
            actionContextFactory,
            projectDescriptorFactory ,
            setSnapshotVersionActionFactory
        )
    }

    @Test
    fun test_run() {
        // GIVEN
        val projectDescriptor: ProjectDescriptor = mock()
        val setSnapshotVersionAction: SetSnapshotVersionAction = mock()

        // WHEN
        whenever(actionContextFactory.create(project.providers)).thenReturn(ACTION_CONTEXT)
        whenever(projectDescriptorFactory.create(extension.projectActionsFactory, extension.scmActions, project.projectDir))
            .thenReturn(projectDescriptor)
        whenever(setSnapshotVersionActionFactory.create(extension)).thenReturn(setSnapshotVersionAction)

        // THEN
        underTest.run()

        verifyNoInteractions(projectDescriptor)

        verify(projectDescriptorFactory).create(extension.projectActionsFactory, extension.scmActions, project.projectDir)

        verify(setSnapshotVersionActionFactory).create(extension)
        verify(setSnapshotVersionAction).execute(projectDescriptor, ACTION_CONTEXT)

        verifyNoMoreInteractions(setSnapshotVersionAction, setSnapshotVersionActionFactory)
    }

}
