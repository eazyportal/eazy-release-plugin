package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.SetReleaseVersionAction
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

internal class SetReleaseVersionTaskTest : EazyReleaseBaseTaskTest<SetReleaseVersionTask>() {

    @Mock
    private lateinit var releaseActionFactory: ReleaseActionFactory

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.SET_RELEASE_VERSION_TASK_NAME,
            SetReleaseVersionTask::class.java,
            releaseActionFactory
        )
    }

    @Disabled("It is not possible to mock inline functions.")
    @Test
    fun test_run() {
        // GIVEN
        val setReleaseVersionAction: SetReleaseVersionAction = mock()

        // WHEN
        whenever(releaseActionFactory.create<SetReleaseVersionAction>(project))
            .thenReturn(setReleaseVersionAction)

        doNothing().whenever(setReleaseVersionAction).execute()

        // THEN
        underTest.run()

        verify(releaseActionFactory).create<SetReleaseVersionAction>(project)
        verify(setReleaseVersionAction).execute()
        verifyNoMoreInteractions(releaseActionFactory, setReleaseVersionAction)
    }

}
