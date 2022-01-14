package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.UpdateScmAction
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

internal class UpdateScmTaskTest : EazyReleaseBaseTaskTest<UpdateScmTask>() {

    @Mock
    private lateinit var updateScmAction: UpdateScmAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(EazyReleasePlugin.UPDATE_SCM_TASK_NAME, UpdateScmTask::class.java, updateScmAction)
            .also {
                it.scmActions.set(scmActions)
                it.scmConfig.set(scmConfig)
            }
    }

    @Test
    fun test_run() {
        // GIVEN
        // WHEN
        // THEN
        underTest.run()

        verify(updateScmAction).scmActions = scmActions
        verify(updateScmAction).scmConfig = scmConfig
        verify(updateScmAction).execute(project.rootDir)
        verifyNoMoreInteractions(updateScmAction)
    }

}
