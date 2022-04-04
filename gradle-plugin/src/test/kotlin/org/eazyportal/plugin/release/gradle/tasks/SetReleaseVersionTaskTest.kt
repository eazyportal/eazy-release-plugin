package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.SetReleaseVersionAction
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin.Companion.SET_RELEASE_VERSION_TASK_NAME
import org.eazyportal.plugin.release.gradle.action.SetReleaseVersionActionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class SetReleaseVersionTaskTest : EazyReleaseBaseTaskTest<SetReleaseVersionTask>() {

    @Mock
    private lateinit var setReleaseVersionActionFactory: SetReleaseVersionActionFactory

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)

        underTest = project.tasks.create(SET_RELEASE_VERSION_TASK_NAME, SetReleaseVersionTask::class.java, setReleaseVersionActionFactory)
    }

    @Test
    fun test_run() {
        // GIVEN
        val setReleaseVersionAction = mock<SetReleaseVersionAction>()

        // WHEN
        whenever(setReleaseVersionActionFactory.create(extension)).thenReturn(setReleaseVersionAction)

        // THEN
        underTest.run()

        verify(setReleaseVersionActionFactory).create(extension)
        verify(setReleaseVersionAction).execute(project.rootDir)
        verifyNoMoreInteractions(setReleaseVersionAction, setReleaseVersionActionFactory)
    }

}
