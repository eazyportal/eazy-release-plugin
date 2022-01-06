package org.eazyportal.plugin.release.core

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.exception.ScmActionException
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider
import org.eazyportal.plugin.release.core.version.model.Version
import org.eazyportal.plugin.release.core.version.model.VersionIncrement
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.File

internal class SetReleaseVersionActionTest {

    companion object {
        private const val GIT_TAG = "0.0.0"
        private val RELEASE_001 = Version(0, 0, 1)
        private val SNAPSHOT_001 = Version(0, 0, 1, Version.DEVELOPMENT_VERSION_SUFFIX)

        @JvmStatic
        fun execute() = listOf(
            Arguments.of(SNAPSHOT_001, listOf("BREAKING CHANGE: message"), VersionIncrement.MAJOR),
            Arguments.of(SNAPSHOT_001, listOf("feature: message"), VersionIncrement.MINOR),
            Arguments.of(SNAPSHOT_001, listOf("fix: message"), VersionIncrement.PATCH),
            Arguments.of(SNAPSHOT_001, listOf("chore: message"), VersionIncrement.NONE),

            Arguments.of(SNAPSHOT_001, listOf("feature!: message"), VersionIncrement.MAJOR),
            Arguments.of(SNAPSHOT_001, listOf("fix!: message"), VersionIncrement.MAJOR),

            Arguments.of(SNAPSHOT_001, listOf("feature: message", "fix: message"), VersionIncrement.MINOR),
            Arguments.of(SNAPSHOT_001, listOf("feature: message", "fix!: message"), VersionIncrement.MAJOR),
        )

        @JvmStatic
        fun execute_withInvalidCommits() = listOf(
            Arguments.of(SNAPSHOT_001, listOf("")),
            Arguments.of(SNAPSHOT_001, listOf("missing commit type")),
            Arguments.of(SNAPSHOT_001, listOf("invalid: commit type"))
        )
    }

    private val workingDir = File("")

    @Mock
    private lateinit var projectActions: ProjectActions
    @Mock
    private lateinit var releaseVersionProvider: ReleaseVersionProvider
    @Mock
    private lateinit var scmActions: ScmActions

    @InjectMocks
    private lateinit var underTest: SetReleaseVersionAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        underTest.also {
            it.conventionalCommitTypes = ConventionalCommitType.DEFAULT_TYPES
            it.scmActions = scmActions
            it.scmConfig = ScmConfig.GIT_FLOW
        }
    }

    @MethodSource("execute")
    @ParameterizedTest
    fun test_execute_withGitFlow(currentVersion: Version, commits: List<String>, expectedVersionIncrement: VersionIncrement) {
        // GIVEN
        underTest.scmConfig = ScmConfig.GIT_FLOW

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(currentVersion)
        whenever(scmActions.getLastTag(workingDir)).thenReturn(GIT_TAG)
        whenever(scmActions.getCommits(workingDir, GIT_TAG)).thenReturn(commits)
        whenever(releaseVersionProvider.provide(eq(currentVersion), any())).thenReturn(RELEASE_001)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("dummy"))

        // THEN
        underTest.execute(workingDir)

        val versionIncrementCaptor = argumentCaptor<VersionIncrement>()

        verify(scmActions).fetch(workingDir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, GIT_TAG)
        verify(releaseVersionProvider).provide(eq(currentVersion), versionIncrementCaptor.capture())
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(scmActions).mergeNoCommit(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).setVersion(RELEASE_001)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verify(scmActions).tag(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, releaseVersionProvider, scmActions)

        assertThat(versionIncrementCaptor.firstValue).isEqualTo(expectedVersionIncrement)
    }

    @MethodSource("execute")
    @ParameterizedTest
    fun test_execute_withTrunkBasedFlow(currentVersion: Version, commits: List<String>, expectedVersionIncrement: VersionIncrement) {
        // GIVEN
        underTest.scmConfig = ScmConfig.TRUNK_BASED_FLOW

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(currentVersion)
        whenever(scmActions.getLastTag(workingDir)).thenReturn(GIT_TAG)
        whenever(scmActions.getCommits(workingDir, GIT_TAG)).thenReturn(commits)
        whenever(releaseVersionProvider.provide(eq(currentVersion), any())).thenReturn(RELEASE_001)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("dummy"))

        // THEN
        underTest.execute(workingDir)

        val versionIncrementCaptor = argumentCaptor<VersionIncrement>()

        verify(scmActions).fetch(workingDir, ScmConfig.TRUNK_BASED_FLOW.remote)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, GIT_TAG)
        verify(releaseVersionProvider).provide(eq(currentVersion), versionIncrementCaptor.capture())
        verify(projectActions).setVersion(RELEASE_001)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verify(scmActions).tag(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, releaseVersionProvider, scmActions)

        assertThat(versionIncrementCaptor.firstValue).isEqualTo(expectedVersionIncrement)
    }

    @Test
    fun test_execute_whenFailedToRetrieveTag() {
        // GIVEN
        val commits = listOf("build: message")

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(SNAPSHOT_001)
        whenever(scmActions.getLastTag(workingDir)).thenAnswer { throw ScmActionException(RuntimeException()) }
        whenever(scmActions.getCommits(workingDir, null)).thenReturn(commits)
        whenever(releaseVersionProvider.provide(eq(SNAPSHOT_001), any())).thenReturn(RELEASE_001)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("dummy"))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).fetch(workingDir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, null)
        verify(releaseVersionProvider).provide(eq(SNAPSHOT_001), any())
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(scmActions).mergeNoCommit(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).setVersion(RELEASE_001)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verify(scmActions).tag(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, releaseVersionProvider, scmActions)
    }

    @Test
    fun test_execute_whenConventionalCommitTypesIsEmpty() {
        // GIVEN
        underTest.conventionalCommitTypes = listOf()

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(SNAPSHOT_001)
        whenever(scmActions.getLastTag(workingDir)).thenReturn(GIT_TAG)
        whenever(scmActions.getCommits(workingDir, GIT_TAG)).thenReturn(listOf("feature: message"))
        whenever(releaseVersionProvider.provide(eq(SNAPSHOT_001), any())).thenReturn(RELEASE_001)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("dummy"))

        // THEN
        underTest.execute(workingDir)

        val versionIncrementCaptor = argumentCaptor<VersionIncrement>()

        verify(scmActions).fetch(workingDir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, GIT_TAG)
        verify(releaseVersionProvider).provide(eq(SNAPSHOT_001), versionIncrementCaptor.capture())
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(scmActions).mergeNoCommit(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).setVersion(RELEASE_001)
        verify(projectActions).scmFilesToCommit()
        verify(scmActions).add(eq(workingDir), any())
        verify(scmActions).commit(eq(workingDir), any())
        verify(scmActions).tag(eq(workingDir), any())
        verifyNoMoreInteractions(projectActions, releaseVersionProvider, scmActions)

        assertThat(versionIncrementCaptor.firstValue).isEqualTo(VersionIncrement.MINOR)
    }

    @MethodSource("execute_withInvalidCommits")
    @ParameterizedTest
    fun test_execute_shouldFail_whenCommitsAreInvalid(currentVersion: Version, commits: List<String>) {
        // GIVEN
        // WHEN
        whenever(projectActions.getVersion()).thenReturn(currentVersion)
        whenever(scmActions.getLastTag(workingDir)).thenReturn(GIT_TAG)
        whenever(scmActions.getCommits(workingDir, GIT_TAG)).thenReturn(commits)

        // THEN
        assertThatThrownBy { underTest.execute(workingDir) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("There are no acceptable commits since the previous release {tag: $GIT_TAG}.")

        verifyNoInteractions(releaseVersionProvider)
        verify(scmActions).fetch(workingDir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, GIT_TAG)
        verifyNoMoreInteractions(projectActions, releaseVersionProvider, scmActions)
    }

}
