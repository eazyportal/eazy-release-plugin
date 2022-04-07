package org.eazyportal.plugin.release.core

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.exception.ScmActionException
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider
import org.eazyportal.plugin.release.core.version.model.VersionFixtures
import org.eazyportal.plugin.release.core.version.model.VersionIncrement
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.File

internal class SetReleaseVersionActionTest {

    private companion object {
        @JvmStatic
        val COMMITS = listOf("fix: message", "test: message")
        const val GIT_TAG = "0.0.0"

        @JvmStatic
        fun invalidVersionIncrement() = listOf(
            Arguments.of(null),
            Arguments.of(VersionIncrement.NONE)
        )
    }

    private val conventionalCommitTypes = ConventionalCommitType.DEFAULT_TYPES
    private val scmActions = mock<ScmActions>()

    @TempDir
    private lateinit var workingDir: File

    @Mock
    private lateinit var projectActionsFactory: ProjectActionsFactory
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
        val projectActions = mock<ProjectActions>()
        val versionIncrement = VersionIncrement.PATCH

        // WHEN
        underTest.scmConfig = ScmConfig.GIT_FLOW

        whenever(projectActionsFactory.create(workingDir)).thenReturn(projectActions)
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.SNAPSHOT_001)
        whenever(scmActions.getLastTag(workingDir)).thenReturn(GIT_TAG)
        whenever(scmActions.getCommits(workingDir, GIT_TAG)).thenReturn(COMMITS)
        whenever(versionIncrementProvider.provide(COMMITS, conventionalCommitTypes)).thenReturn(versionIncrement)
        whenever(releaseVersionProvider.provide(VersionFixtures.SNAPSHOT_001, versionIncrement)).thenReturn(VersionFixtures.RELEASE_001)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("dummy"))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).fetch(workingDir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActionsFactory, times(2)).create(workingDir)
        verify(scmActions).getSubmodules(workingDir)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, GIT_TAG)
        verify(versionIncrementProvider).provide(COMMITS, conventionalCommitTypes)
        verify(releaseVersionProvider).provide(VersionFixtures.SNAPSHOT_001, versionIncrement)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(scmActions).mergeNoCommit(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).setVersion(VersionFixtures.RELEASE_001)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verify(scmActions).tag(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, projectActionsFactory, releaseVersionProvider, scmActions, versionIncrementProvider)
    }

    @Test
    fun test_execute_withTrunkBasedFlow() {
        // GIVEN
        val projectActions = mock<ProjectActions>()
        val versionIncrement = VersionIncrement.PATCH

        // WHEN
        underTest.scmConfig = ScmConfig.TRUNK_BASED_FLOW

        whenever(projectActionsFactory.create(workingDir)).thenReturn(projectActions)
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.SNAPSHOT_001)
        whenever(scmActions.getLastTag(workingDir)).thenReturn(GIT_TAG)
        whenever(scmActions.getCommits(workingDir, GIT_TAG)).thenReturn(COMMITS)
        whenever(versionIncrementProvider.provide(COMMITS, conventionalCommitTypes)).thenReturn(versionIncrement)
        whenever(releaseVersionProvider.provide(VersionFixtures.SNAPSHOT_001, versionIncrement)).thenReturn(VersionFixtures.RELEASE_001)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("dummy"))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).fetch(workingDir, ScmConfig.TRUNK_BASED_FLOW.remote)
        verify(scmActions).getSubmodules(workingDir)
        verify(projectActionsFactory, times(2)).create(workingDir)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, GIT_TAG)
        verify(versionIncrementProvider).provide(COMMITS, conventionalCommitTypes)
        verify(releaseVersionProvider).provide(VersionFixtures.SNAPSHOT_001, versionIncrement)
        verify(projectActions).setVersion(VersionFixtures.RELEASE_001)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verify(scmActions).tag(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, projectActionsFactory, releaseVersionProvider, scmActions, versionIncrementProvider)
    }

    @Test
    fun test_execute_whenFailedToRetrieveTag() {
        // GIVEN
        val projectActions = mock<ProjectActions>()
        val versionIncrement = VersionIncrement.PATCH

        // WHEN
        whenever(projectActionsFactory.create(workingDir)).thenReturn(projectActions)
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.SNAPSHOT_001)
        whenever(scmActions.getLastTag(workingDir)).thenAnswer { throw ScmActionException(RuntimeException()) }
        whenever(scmActions.getCommits(workingDir, null)).thenReturn(COMMITS)
        whenever(versionIncrementProvider.provide(COMMITS, conventionalCommitTypes)).thenReturn(versionIncrement)
        whenever(releaseVersionProvider.provide(VersionFixtures.SNAPSHOT_001, versionIncrement)).thenReturn(VersionFixtures.RELEASE_001)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("dummy"))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).fetch(workingDir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(scmActions).getSubmodules(workingDir)
        verify(projectActionsFactory, times(2)).create(workingDir)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, null)
        verify(versionIncrementProvider).provide(COMMITS, conventionalCommitTypes)
        verify(releaseVersionProvider).provide(VersionFixtures.SNAPSHOT_001, versionIncrement)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(scmActions).mergeNoCommit(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).setVersion(VersionFixtures.RELEASE_001)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verify(scmActions).tag(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, projectActionsFactory, releaseVersionProvider, scmActions, versionIncrementProvider)
    }

    @MethodSource("invalidVersionIncrement")
    @ParameterizedTest
    fun test_execute_shouldFail_whenVersionIncrementIsInvalid(versionIncrement: VersionIncrement?) {
        // GIVEN
        val projectActions = mock<ProjectActions>()

        // WHEN
        whenever(projectActionsFactory.create(workingDir)).thenReturn(projectActions)
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.SNAPSHOT_001)
        whenever(scmActions.getLastTag(workingDir)).thenAnswer { throw ScmActionException(RuntimeException()) }
        whenever(scmActions.getCommits(workingDir, null)).thenReturn(COMMITS)
        whenever(versionIncrementProvider.provide(COMMITS, conventionalCommitTypes)).thenReturn(versionIncrement)

        // THEN
        assertThatThrownBy { underTest.execute(workingDir) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("There are no acceptable commits.")

        verifyNoInteractions(releaseVersionProvider)
        verify(scmActions).fetch(workingDir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(scmActions).getSubmodules(workingDir)
        verify(projectActionsFactory).create(workingDir)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, null)
        verify(versionIncrementProvider).provide(COMMITS, conventionalCommitTypes)
        verifyNoMoreInteractions(projectActions, projectActionsFactory, scmActions, versionIncrementProvider)
    }

}
