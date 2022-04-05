package org.eazyportal.plugin.release.core.action

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
import org.eazyportal.plugin.release.core.version.model.VersionIncrement.NONE
import org.eazyportal.plugin.release.core.version.model.VersionIncrement.PATCH
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
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
        const val SUBMODULE_NAME = "ui"

        @JvmStatic
        fun invalidVersionIncrement() = listOf(
            Arguments.of(null),
            Arguments.of(NONE)
        )
    }

    @TempDir
    private lateinit var workingDir: File

    private val conventionalCommitTypes = ConventionalCommitType.DEFAULT_TYPES
    @Mock
    private lateinit var projectActionsFactory: ProjectActionsFactory
    @Mock
    private lateinit var releaseVersionProvider: ReleaseVersionProvider
    @Mock
    private lateinit var scmActions: ScmActions
    @Mock
    private lateinit var versionIncrementProvider: VersionIncrementProvider

    private lateinit var underTest: SetReleaseVersionAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_execute_withGitFlow() {
        // GIVEN
        val submoduleDir = workingDir.resolve(SUBMODULE_NAME)

        val projectActions: ProjectActions = mock()
        val versionIncrement = PATCH

        underTest = createSetReleaseVersionAction(ScmConfig.GIT_FLOW)

        // WHEN
        whenever(scmActions.getSubmodules(workingDir)).thenReturn(listOf(SUBMODULE_NAME))

        whenever(projectActionsFactory.create(any())).thenReturn(projectActions)
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.SNAPSHOT_001)

        whenever(scmActions.getLastTag(submoduleDir)).then {  throw ScmActionException(null) }
        whenever(scmActions.getCommits(submoduleDir, null)).thenReturn(COMMITS)
        whenever(scmActions.getLastTag(workingDir)).thenReturn(GIT_TAG)
        whenever(scmActions.getCommits(workingDir, GIT_TAG)).thenReturn(COMMITS)

        whenever(versionIncrementProvider.provide(COMMITS, conventionalCommitTypes)).thenReturn(versionIncrement)
        whenever(releaseVersionProvider.provide(VersionFixtures.SNAPSHOT_001, versionIncrement)).thenReturn(VersionFixtures.RELEASE_001)

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).getSubmodules(workingDir)

        verify(projectActionsFactory, times(2)).create(submoduleDir)
        verify(scmActions).getLastTag(submoduleDir)
        verify(scmActions).getCommits(submoduleDir, null)

        verify(projectActionsFactory, times(2)).create(workingDir)
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, GIT_TAG)

        verify(projectActions, times(2)).getVersion()
        verify(versionIncrementProvider, times(2)).provide(COMMITS, conventionalCommitTypes)

        verify(scmActions).checkout(submoduleDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(scmActions).mergeNoCommit(submoduleDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(scmActions).mergeNoCommit(workingDir, ScmConfig.GIT_FLOW.featureBranch)

        verify(releaseVersionProvider, times(2)).provide(VersionFixtures.SNAPSHOT_001, versionIncrement)
        verify(projectActions, times(2)).setVersion(VersionFixtures.RELEASE_001)

        verifyNoMoreInteractions(projectActions, projectActionsFactory, releaseVersionProvider, scmActions, versionIncrementProvider)
    }

    @Test
    fun test_execute_withTrunkBasedFlow() {
        // GIVEN
        val submoduleDir = workingDir.resolve(SUBMODULE_NAME)

        val projectActions: ProjectActions = mock()
        val versionIncrement = PATCH

        underTest = createSetReleaseVersionAction(ScmConfig.TRUNK_BASED_FLOW)

        // WHEN
        whenever(scmActions.getSubmodules(workingDir)).thenReturn(listOf(SUBMODULE_NAME))

        whenever(projectActionsFactory.create(any())).thenReturn(projectActions)
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.SNAPSHOT_001)

        whenever(scmActions.getLastTag(submoduleDir)).then {  throw ScmActionException(null) }
        whenever(scmActions.getCommits(submoduleDir, null)).thenReturn(COMMITS)
        whenever(scmActions.getLastTag(workingDir)).thenReturn(GIT_TAG)
        whenever(scmActions.getCommits(workingDir, GIT_TAG)).thenReturn(COMMITS)

        whenever(versionIncrementProvider.provide(COMMITS, conventionalCommitTypes)).thenReturn(versionIncrement)
        whenever(releaseVersionProvider.provide(VersionFixtures.SNAPSHOT_001, versionIncrement)).thenReturn(VersionFixtures.RELEASE_001)

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).getSubmodules(workingDir)

        verify(projectActionsFactory, times(2)).create(submoduleDir)
        verify(scmActions).getLastTag(submoduleDir)
        verify(scmActions).getCommits(submoduleDir, null)

        verify(projectActionsFactory, times(2)).create(workingDir)
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, GIT_TAG)

        verify(projectActions, times(2)).getVersion()
        verify(versionIncrementProvider, times(2)).provide(COMMITS, conventionalCommitTypes)

        verify(releaseVersionProvider, times(2)).provide(VersionFixtures.SNAPSHOT_001, versionIncrement)
        verify(projectActions, times(2)).setVersion(VersionFixtures.RELEASE_001)

        verifyNoMoreInteractions(projectActions, projectActionsFactory, releaseVersionProvider, scmActions, versionIncrementProvider)
    }

    @MethodSource("invalidVersionIncrement")
    @ParameterizedTest
    fun test_execute_shouldFail_whenVersionIncrementIsInvalid(versionIncrement: VersionIncrement?) {
        // GIVEN
        val projectActions: ProjectActions = mock()

        underTest = createSetReleaseVersionAction()

        // WHEN
        whenever(projectActionsFactory.create(workingDir)).thenReturn(projectActions)
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.SNAPSHOT_001)
        whenever(scmActions.getLastTag(workingDir)).then { throw ScmActionException(RuntimeException()) }
        whenever(scmActions.getCommits(workingDir, null)).thenReturn(COMMITS)
        whenever(versionIncrementProvider.provide(COMMITS, conventionalCommitTypes)).thenReturn(versionIncrement)

        // THEN
        assertThatThrownBy { underTest.execute(workingDir) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("There are no acceptable commits.")

        verifyNoInteractions(releaseVersionProvider)
        verify(scmActions).getSubmodules(workingDir)
        verify(projectActionsFactory).create(workingDir)
        verify(projectActions).getVersion()
        verify(scmActions).getLastTag(workingDir)
        verify(scmActions).getCommits(workingDir, null)
        verify(versionIncrementProvider).provide(COMMITS, conventionalCommitTypes)
        verifyNoMoreInteractions(projectActions, projectActionsFactory, scmActions, versionIncrementProvider)
    }

    private fun createSetReleaseVersionAction(scmConfig: ScmConfig = ScmConfig.GIT_FLOW) =
        SetReleaseVersionAction(
            conventionalCommitTypes,
            projectActionsFactory,
            releaseVersionProvider,
            scmActions,
            scmConfig,
            versionIncrementProvider
        )

}
