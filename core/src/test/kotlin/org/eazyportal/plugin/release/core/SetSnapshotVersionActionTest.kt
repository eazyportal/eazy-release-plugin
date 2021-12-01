package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider
import org.eazyportal.plugin.release.core.version.model.Version
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.File

internal class SetSnapshotVersionActionTest {

    companion object {
        @JvmStatic
        private val RELEASE_001 = Version(0, 0, 1)
        @JvmStatic
        private val SNAPSHOT_002 = Version(0, 0, 2, Version.DEVELOPMENT_VERSION_SUFFIX)
    }

    private val workingDir = File("")

    @Mock
    private lateinit var projectActions: ProjectActions
    @Mock
    private lateinit var snapshotVersionProvider: SnapshotVersionProvider
    @Mock
    private lateinit var scmActions: ScmActions

    @InjectMocks
    private lateinit var underTest: SetSnapshotVersionAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_execute_withGitFlow() {
        // GIVEN
        underTest.scmConfig = ScmConfig.GIT_FLOW

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(RELEASE_001)
        whenever(snapshotVersionProvider.provide(RELEASE_001)).thenReturn(SNAPSHOT_002)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("."))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(projectActions).getVersion()
        verify(snapshotVersionProvider).provide(RELEASE_001)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(scmActions).mergeNoCommit(workingDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(projectActions).setVersion(SNAPSHOT_002)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, scmActions, snapshotVersionProvider)
    }

    @Test
    fun test_execute_withTrunkBasedFlow() {
        // GIVEN
        underTest.scmConfig = ScmConfig.TRUNK_BASED_FLOW

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(RELEASE_001)
        whenever(snapshotVersionProvider.provide(RELEASE_001)).thenReturn(SNAPSHOT_002)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("."))

        // THEN
        underTest.execute(workingDir)

        verify(projectActions).getVersion()
        verify(snapshotVersionProvider).provide(RELEASE_001)
        verify(projectActions).setVersion(SNAPSHOT_002)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, scmActions, snapshotVersionProvider)
    }

}
