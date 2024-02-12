package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction
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
import java.io.File

internal class FinalizeSnapshotVersionTaskTest : EazyReleaseBaseTaskTest<FinalizeSnapshotVersionTask>() {

    @Mock
    private lateinit var releaseActionFactory: ReleaseActionFactory

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(
            EazyReleasePlugin.FINALIZE_SNAPSHOT_VERSION_TASK_NAME,
            FinalizeSnapshotVersionTask::class.java,
            releaseActionFactory
        )
    }

    @Disabled("It is not possible to mock inline functions.")
    @Test
    fun test_run() {
        // GIVEN
        val finalizeSnapshotVersionAction: FinalizeSnapshotVersionAction<File> = mock()

        // WHEN
        whenever(releaseActionFactory.create<FinalizeSnapshotVersionAction<File>>(project))
            .thenReturn(finalizeSnapshotVersionAction)

        doNothing().whenever(finalizeSnapshotVersionAction).execute()

        // THEN
        underTest.run()

        verify(releaseActionFactory).create<FinalizeSnapshotVersionAction<File>>(project)
        verify(finalizeSnapshotVersionAction).execute()
        verifyNoMoreInteractions(finalizeSnapshotVersionAction, releaseActionFactory)
    }

}
