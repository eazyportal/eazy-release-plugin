package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction
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

internal class SetSnapshotVersionTaskTest : EazyReleaseBaseTaskTest<SetSnapshotVersionTask>() {

    @Mock
    private lateinit var releaseActionFactory: ReleaseActionFactory

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.SET_SNAPSHOT_VERSION_TASK_NAME,
            SetSnapshotVersionTask::class.java,
            releaseActionFactory
        )
    }

    @Disabled("It is not possible to mock inline functions.")
    @Test
    fun test_run() {
        // GIVEN
        val setSnapshotVersionAction: SetSnapshotVersionAction = mock()

        // WHEN
        whenever(releaseActionFactory.create<SetSnapshotVersionAction>(project))
            .thenReturn(setSnapshotVersionAction)

        doNothing().whenever(setSnapshotVersionAction).execute()

        // THEN
        underTest.run()

        verify(releaseActionFactory).create<SetSnapshotVersionAction>(project)
        verify(setSnapshotVersionAction).execute()
        verifyNoMoreInteractions(releaseActionFactory, setSnapshotVersionAction)
    }

}
