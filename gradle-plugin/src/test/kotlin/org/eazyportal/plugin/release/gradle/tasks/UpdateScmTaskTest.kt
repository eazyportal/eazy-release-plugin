package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.UpdateScmAction
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

internal class UpdateScmTaskTest : EazyReleaseBaseTaskTest<UpdateScmTask>() {

    @Mock
    private lateinit var releaseActionFactory: ReleaseActionFactory

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.UPDATE_SCM_TASK_NAME,
            UpdateScmTask::class.java,
            releaseActionFactory
        )
    }

    @Disabled("It is not possible to mock inline functions.")
    @Test
    fun test_run() {
        // GIVEN
        val updateScmAction: UpdateScmAction = mock()

        // WHEN
        whenever(releaseActionFactory.create<UpdateScmAction>(project))
            .thenReturn(updateScmAction)

        doNothing().whenever(updateScmAction).execute()

        // THEN
        underTest.run()

        verify(releaseActionFactory).create<UpdateScmAction>(project)
        verify(updateScmAction).execute()
        verifyNoMoreInteractions(releaseActionFactory, updateScmAction)
    }

}
