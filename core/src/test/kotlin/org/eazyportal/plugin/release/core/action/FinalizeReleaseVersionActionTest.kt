package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.version.model.Version
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.File

internal class FinalizeReleaseVersionActionTest {

    private companion object {
        const val FILE_TO_COMMIT = "."
        const val SUBMODULE_NAME = "ui"
        @JvmStatic
        val RELEASE_001 = Version(0, 0, 1)
    }

    @TempDir
    private lateinit var workingDir: File

    @Mock
    private lateinit var projectActionsFactory: ProjectActionsFactory
    @Mock
    private lateinit var scmActions: ScmActions

    @InjectMocks
    private lateinit var underTest: FinalizeReleaseVersionAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_execute() {
        // GIVEN
        val projectActions: ProjectActions = mock()

        // WHEN
        whenever(projectActionsFactory.create(any())).thenReturn(projectActions)
        whenever(projectActions.getVersion()).thenReturn(RELEASE_001)
        whenever(scmActions.getSubmodules(workingDir)).thenReturn(listOf(SUBMODULE_NAME))
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf(FILE_TO_COMMIT))

        // THEN
        underTest.execute(workingDir)

        verify(projectActionsFactory, times(2)).create(workingDir)
        verify(projectActions).getVersion()
        verify(scmActions).getSubmodules(workingDir)
        verify(projectActionsFactory).create(workingDir.resolve(SUBMODULE_NAME))
        verify(projectActions, times(2)).scmFilesToCommit()
        verify(scmActions, times(2)).add(any(), eq(FILE_TO_COMMIT))
        verify(scmActions, times(2)).commit(any(), eq("Release version: $RELEASE_001"))
        verify(scmActions).tag(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, projectActionsFactory, scmActions)
    }

}
