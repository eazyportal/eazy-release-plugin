package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.ProjectDescriptorFactory
import org.eazyportal.plugin.release.core.action.UpdateScmAction
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.action.UpdateScmActionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class UpdateScmTaskTest : EazyReleaseBaseTaskTest<UpdateScmTask>() {

    @Mock
    private lateinit var projectDescriptorFactory: ProjectDescriptorFactory
    @Mock
    private lateinit var updateScmActionFactory: UpdateScmActionFactory

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.UPDATE_SCM_TASK_NAME,
            UpdateScmTask::class.java,
            projectDescriptorFactory,
            updateScmActionFactory
        )
    }

    @Test
    fun test_run() {
        // GIVEN
        val projectDescriptor: ProjectDescriptor = mock()
        val updateScmAction: UpdateScmAction = mock()

        // WHEN
        whenever(projectDescriptorFactory.create(extension.projectActionsFactory, extension.scmActions, project.projectDir))
            .thenReturn(projectDescriptor)
        whenever(updateScmActionFactory.create(extension)).thenReturn(updateScmAction)

        // THEN
        underTest.run()

        verifyNoInteractions(projectDescriptor)

        verify(projectDescriptorFactory).create(extension.projectActionsFactory, extension.scmActions, project.projectDir)

        verify(updateScmActionFactory).create(extension)
        verify(updateScmAction).execute(projectDescriptor)

        verifyNoMoreInteractions(updateScmAction, updateScmActionFactory)
    }

}
