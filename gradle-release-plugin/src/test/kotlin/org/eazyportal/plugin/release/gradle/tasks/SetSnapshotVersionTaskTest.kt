package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.SetSnapshotVersionAction
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

internal class SetSnapshotVersionTaskTest {

    private val project = ProjectBuilder.builder()
        .build()

    @Mock
    private lateinit var setSnapshotVersionAction: SetSnapshotVersionAction

    private lateinit var underTest: SetSnapshotVersionTask

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(EazyReleasePlugin.UPDATE_SCM_TASK_NAME, SetSnapshotVersionTask::class.java, setSnapshotVersionAction)
    }

    @Test
    fun test_run() {
        // GIVEN
        // WHEN
        // THEN
        underTest.run()

        verify(setSnapshotVersionAction).execute(project.rootDir)
        verifyNoMoreInteractions(setSnapshotVersionAction)
    }

}
