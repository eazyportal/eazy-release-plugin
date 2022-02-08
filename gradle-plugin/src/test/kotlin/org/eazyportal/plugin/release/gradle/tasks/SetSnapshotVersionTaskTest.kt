package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.SetSnapshotVersionAction
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

internal class SetSnapshotVersionTaskTest : EazyReleaseBaseTaskTest<SetSnapshotVersionTask>() {

    @Mock
    private lateinit var setSnapshotVersionAction: SetSnapshotVersionAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(EazyReleasePlugin.UPDATE_SCM_TASK_NAME, SetSnapshotVersionTask::class.java, setSnapshotVersionAction)
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

        verify(setSnapshotVersionAction).scmActions = scmActions
        verify(setSnapshotVersionAction).scmConfig = scmConfig
        verify(setSnapshotVersionAction).execute(project.rootDir)
        verifyNoMoreInteractions(setSnapshotVersionAction)
    }

}
