package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.exception.ScmActionException
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider
import org.eazyportal.plugin.release.core.version.model.Version
import org.eazyportal.plugin.release.core.version.model.VersionIncrement
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.provider.Arguments
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.File

internal class SetReleaseVersionActionTest {

    companion object {
        private const val GIT_TAG = "0.0.0"
        private val RELEASE_001 = Version(0, 0, 1)
        private val SNAPSHOT_001 = Version(0, 0, 1, Version.DEVELOPMENT_VERSION_SUFFIX)
    }

    private val conventionalCommitTypes = ConventionalCommitType.DEFAULT_TYPES
    private val scmActions: ScmActions = mock(ScmActions::class.java)
    private val workingDir = File("")

    @Mock
    private lateinit var projectActions: ProjectActions
    @Mock
    private lateinit var releaseVersionProvider: ReleaseVersionProvider
    @Mock
    private lateinit var versionIncrementProvider: VersionIncrementProvider

    @InjectMocks
    private lateinit var underTest: SetReleaseVersionAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest.also {
            it.conventionalCommitTypes = conventionalCommitTypes
            it.scmActions = scmActions
            it.scmConfig = ScmConfig.GIT_FLOW
        }
    }

    @Test
    fun test_execute_withGitFlow() {
        // GIVEN
        val commits = listOf("build: message", "test: message")
        val versionIncrement = VersionIncrement.NONE

        underTest.scmConfig = ScmConfig.GIT_FLOW

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(SNAPSHOT_001)
        whenever(scmActions.getLastTag(workingDir)).thenReturn(GIT_TAG)
        whenever(scmActions.getCommits(workingDir, GIT_TAG)).thenReturn(commits)
        whenever(versionIncrementProvider.provide(commits, conventionalCommitTypes)).thenReturn(versionIncrement)
        whenever(releaseVersionProvider.provide(SNAPSHOT_001, versionIncrement)).thenReturn(RELEASE_001)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("dummy"))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).fetch(workingDir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, GIT_TAG)
        verify(versionIncrementProvider).provide(commits, conventionalCommitTypes)
        verify(releaseVersionProvider).provide(SNAPSHOT_001, versionIncrement)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(scmActions).mergeNoCommit(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).setVersion(RELEASE_001)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verify(scmActions).tag(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, releaseVersionProvider, scmActions, versionIncrementProvider)
    }

    @Test
    fun test_execute_withTrunkBasedFlow() {
        // GIVEN
        val commits = listOf("build: message", "test: message")
        val versionIncrement = VersionIncrement.NONE

        underTest.scmConfig = ScmConfig.TRUNK_BASED_FLOW

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(SNAPSHOT_001)
        whenever(scmActions.getLastTag(workingDir)).thenReturn(GIT_TAG)
        whenever(scmActions.getCommits(workingDir, GIT_TAG)).thenReturn(commits)
        whenever(versionIncrementProvider.provide(commits, conventionalCommitTypes)).thenReturn(versionIncrement)
        whenever(releaseVersionProvider.provide(SNAPSHOT_001, versionIncrement)).thenReturn(RELEASE_001)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("dummy"))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).fetch(workingDir, ScmConfig.TRUNK_BASED_FLOW.remote)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, GIT_TAG)
        verify(versionIncrementProvider).provide(commits, conventionalCommitTypes)
        verify(releaseVersionProvider).provide(SNAPSHOT_001, versionIncrement)
        verify(projectActions).setVersion(RELEASE_001)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verify(scmActions).tag(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, releaseVersionProvider, scmActions, versionIncrementProvider)
    }

    @Test
    fun test_execute_whenFailedToRetrieveTag() {
        // GIVEN
        val commits = listOf("build: message", "test: message")
        val versionIncrement = VersionIncrement.NONE

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(SNAPSHOT_001)
        whenever(scmActions.getLastTag(workingDir)).thenAnswer { throw ScmActionException(RuntimeException()) }
        whenever(scmActions.getCommits(workingDir, null)).thenReturn(commits)
        whenever(versionIncrementProvider.provide(commits, conventionalCommitTypes)).thenReturn(versionIncrement)
        whenever(releaseVersionProvider.provide(SNAPSHOT_001, versionIncrement)).thenReturn(RELEASE_001)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("dummy"))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).fetch(workingDir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, null)
        verify(versionIncrementProvider).provide(commits, conventionalCommitTypes)
        verify(releaseVersionProvider).provide(SNAPSHOT_001, versionIncrement)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(scmActions).mergeNoCommit(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).setVersion(RELEASE_001)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verify(scmActions).tag(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, releaseVersionProvider, scmActions, versionIncrementProvider)
    }

}
