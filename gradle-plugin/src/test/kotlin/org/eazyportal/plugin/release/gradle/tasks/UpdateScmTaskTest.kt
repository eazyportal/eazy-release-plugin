package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.UpdateScmAction
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.action.UpdateScmActionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class UpdateScmTaskTest : EazyReleaseBaseTaskTest<UpdateScmTask>() {

    @Mock
    private lateinit var updateScmActionFactory: UpdateScmActionFactory

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(EazyReleasePlugin.UPDATE_SCM_TASK_NAME, UpdateScmTask::class.java, updateScmActionFactory)
    }

    @Test
    fun test_run() {
        // GIVEN
        val updateScmAction = mock<UpdateScmAction>()

        // WHEN
        whenever(updateScmActionFactory.create(extension)).thenReturn(updateScmAction)

        // THEN
        underTest.run()

        verify(updateScmActionFactory).create(extension)
        verify(updateScmAction).execute(project.rootDir)
        verifyNoMoreInteractions(updateScmAction, updateScmActionFactory)
    }

}
