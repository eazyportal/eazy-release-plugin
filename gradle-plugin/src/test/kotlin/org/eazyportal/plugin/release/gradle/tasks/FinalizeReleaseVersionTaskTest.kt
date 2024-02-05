package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.FixtureValues.ACTION_CONTEXT
import org.eazyportal.plugin.release.core.ProjectDescriptorFactory
import org.eazyportal.plugin.release.core.action.FinalizeReleaseVersionAction
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.action.ActionContextFactory
import org.eazyportal.plugin.release.gradle.action.FinalizeReleaseVersionActionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class FinalizeReleaseVersionTaskTest : EazyReleaseBaseTaskTest<FinalizeReleaseVersionTask>() {

    @Mock
    private lateinit var actionContextFactory: ActionContextFactory
    @Mock
    private lateinit var projectDescriptorFactory: ProjectDescriptorFactory
    @Mock
    private lateinit var finalizeReleaseVersionActionFactory: FinalizeReleaseVersionActionFactory

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.FINALIZE_RELEASE_VERSION_TASK_NAME,
            FinalizeReleaseVersionTask::class.java,
            actionContextFactory,
            projectDescriptorFactory,
            finalizeReleaseVersionActionFactory
        )
    }

    @Test
    fun test_run() {
        // GIVEN
        val finalizeReleaseVersionAction: FinalizeReleaseVersionAction = mock()
        val projectDescriptor: ProjectDescriptor = mock()

        // WHEN
        whenever(actionContextFactory.create(project.providers)).thenReturn(ACTION_CONTEXT)
        whenever(projectDescriptorFactory.create(extension.projectActionsFactory, extension.scmActions, project.projectDir))
            .thenReturn(projectDescriptor)
        whenever(finalizeReleaseVersionActionFactory.create(extension)).thenReturn(finalizeReleaseVersionAction)

        // THEN
        underTest.run()

        verifyNoInteractions(projectDescriptor)

        verify(projectDescriptorFactory).create(extension.projectActionsFactory, extension.scmActions, project.projectDir)

        verify(finalizeReleaseVersionActionFactory).create(extension)
        verify(finalizeReleaseVersionAction).execute(projectDescriptor, ACTION_CONTEXT)

        verifyNoMoreInteractions(finalizeReleaseVersionAction, finalizeReleaseVersionActionFactory, projectDescriptorFactory)
    }

}
