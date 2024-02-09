package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.FinalizeReleaseVersionAction
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

internal class FinalizeReleaseVersionTaskTest : EazyReleaseBaseTaskTest<FinalizeReleaseVersionTask>() {

    @Mock
    private lateinit var releaseActionFactory: ReleaseActionFactory

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.FINALIZE_RELEASE_VERSION_TASK_NAME,
            FinalizeReleaseVersionTask::class.java,
            releaseActionFactory
        )
    }

    @Disabled("It is not possible to mock inline functions.")
    @Test
    fun test_run() {
        // GIVEN
        val finalizeReleaseVersionAction: FinalizeReleaseVersionAction = mock()

        // WHEN
        whenever(releaseActionFactory.create<FinalizeReleaseVersionAction>(project))
            .thenReturn(finalizeReleaseVersionAction)

        doNothing().whenever(finalizeReleaseVersionAction).execute()

        // THEN
        underTest.run()

        verify(releaseActionFactory).create<FinalizeReleaseVersionAction>(project)
        verify(finalizeReleaseVersionAction).execute()
        verifyNoMoreInteractions(finalizeReleaseVersionAction, releaseActionFactory)
    }

}
