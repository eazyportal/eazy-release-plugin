package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.ProjectDescriptorFactory
import org.eazyportal.plugin.release.core.action.FinalizeReleaseVersionAction
import org.eazyportal.plugin.release.core.action.PrepareRepositoryForReleaseAction
import org.eazyportal.plugin.release.core.action.SetReleaseVersionAction
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.action.FinalizeReleaseVersionActionFactory
import org.eazyportal.plugin.release.gradle.action.PrepareRepositoryForReleaseActionFactory
import org.eazyportal.plugin.release.gradle.action.SetReleaseVersionActionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class SetReleaseVersionTaskTest : EazyReleaseBaseTaskTest<SetReleaseVersionTask>() {

    @Mock
    private lateinit var projectDescriptorFactory: ProjectDescriptorFactory
    @Mock
    private lateinit var prepareRepositoryForReleaseActionFactory: PrepareRepositoryForReleaseActionFactory
    @Mock
    private lateinit var setReleaseVersionActionFactory: SetReleaseVersionActionFactory
    @Mock
    private lateinit var finalizeReleaseVersionActionFactory: FinalizeReleaseVersionActionFactory

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.SET_RELEASE_VERSION_TASK_NAME,
            SetReleaseVersionTask::class.java,
            projectDescriptorFactory,
            prepareRepositoryForReleaseActionFactory,
            setReleaseVersionActionFactory,
            finalizeReleaseVersionActionFactory
        )
    }

    @Test
    fun test_run() {
        // GIVEN
        val finalizeReleaseVersionAction: FinalizeReleaseVersionAction = mock()
        val prepareRepositoryForReleaseAction: PrepareRepositoryForReleaseAction = mock()
        val projectDescriptor: ProjectDescriptor = mock()
        val setReleaseVersionAction: SetReleaseVersionAction = mock()

        // WHEN
        whenever(prepareRepositoryForReleaseActionFactory.create(extension)).thenReturn(prepareRepositoryForReleaseAction)
        whenever(projectDescriptorFactory.create(extension.projectActionsFactory, extension.scmActions, project.projectDir))
            .thenReturn(projectDescriptor)
        whenever(setReleaseVersionActionFactory.create(extension)).thenReturn(setReleaseVersionAction)
        whenever(finalizeReleaseVersionActionFactory.create(extension)).thenReturn(finalizeReleaseVersionAction)

        // THEN
        underTest.run()

        verifyNoInteractions(projectDescriptor)

        verify(prepareRepositoryForReleaseActionFactory).create(extension)
        verify(prepareRepositoryForReleaseAction).execute(project.projectDir)

        verify(projectDescriptorFactory).create(extension.projectActionsFactory, extension.scmActions, project.projectDir)

        verify(setReleaseVersionActionFactory).create(extension)
        verify(setReleaseVersionAction).execute(projectDescriptor)

        verify(finalizeReleaseVersionActionFactory).create(extension)
        verify(finalizeReleaseVersionAction).execute(projectDescriptor)

        verifyNoMoreInteractions(
            finalizeReleaseVersionAction, finalizeReleaseVersionActionFactory,
            prepareRepositoryForReleaseAction, prepareRepositoryForReleaseActionFactory,
            projectDescriptorFactory,
            setReleaseVersionAction, setReleaseVersionActionFactory
        )
    }

}
