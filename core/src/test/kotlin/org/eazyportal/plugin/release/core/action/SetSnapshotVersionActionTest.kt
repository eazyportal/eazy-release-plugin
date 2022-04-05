package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider
import org.eazyportal.plugin.release.core.version.model.VersionFixtures
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.File

internal class SetSnapshotVersionActionTest {

    companion object {
        const val SUBMODULE_NAME = "ui"
    }

    @TempDir
    private lateinit var workingDir: File

    @Mock
    private lateinit var projectActionsFactory: ProjectActionsFactory
    @Mock
    private lateinit var scmActions: ScmActions
    @Mock
    private lateinit var snapshotVersionProvider: SnapshotVersionProvider

    private lateinit var underTest: SetSnapshotVersionAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_execute_withGitFlow() {
        // GIVEN
        val submoduleDir = workingDir.resolve(SUBMODULE_NAME)

        val projectActions: ProjectActions = mock()

        underTest = createSetSnapshotVersionAction(ScmConfig.GIT_FLOW)

        // WHEN
        whenever(scmActions.getSubmodules(workingDir)).thenReturn(listOf(SUBMODULE_NAME))

        whenever(projectActionsFactory.create(any())).thenReturn(projectActions)
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.RELEASE_001)
        whenever(snapshotVersionProvider.provide(VersionFixtures.RELEASE_001)).thenReturn(VersionFixtures.SNAPSHOT_002)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("."))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).getSubmodules(workingDir)

        verify(projectActionsFactory).create(submoduleDir)
        verify(projectActionsFactory).create(workingDir)

        verify(projectActions, times(2)).getVersion()
        verify(snapshotVersionProvider, times(2)).provide(VersionFixtures.RELEASE_001)

        verify(scmActions).checkout(submoduleDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(scmActions).mergeNoCommit(submoduleDir, ScmConfig.GIT_FLOW.releaseBranch)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(scmActions).mergeNoCommit(workingDir, ScmConfig.GIT_FLOW.releaseBranch)

        verify(projectActions, times(2)).setVersion(VersionFixtures.SNAPSHOT_002)

        verifyNoMoreInteractions(projectActions, projectActionsFactory, scmActions, snapshotVersionProvider)
    }

    @Test
    fun test_execute_withTrunkBasedFlow() {
        // GIVEN
        val submoduleDir = workingDir.resolve(SUBMODULE_NAME)

        val projectActions: ProjectActions = mock()

        underTest = createSetSnapshotVersionAction(ScmConfig.TRUNK_BASED_FLOW)

        // WHEN
        whenever(scmActions.getSubmodules(workingDir)).thenReturn(listOf(SUBMODULE_NAME))

        whenever(projectActionsFactory.create(any())).thenReturn(projectActions)
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.RELEASE_001)
        whenever(snapshotVersionProvider.provide(VersionFixtures.RELEASE_001)).thenReturn(VersionFixtures.SNAPSHOT_002)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf("."))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).getSubmodules(workingDir)

        verify(projectActionsFactory).create(submoduleDir)
        verify(projectActionsFactory).create(workingDir)

        verify(projectActions, times(2)).getVersion()
        verify(snapshotVersionProvider, times(2)).provide(VersionFixtures.RELEASE_001)

        verify(projectActions, times(2)).setVersion(VersionFixtures.SNAPSHOT_002)

        verifyNoMoreInteractions(projectActions, projectActionsFactory, scmActions, snapshotVersionProvider)
    }

    private fun createSetSnapshotVersionAction(scmConfig: ScmConfig = ScmConfig.GIT_FLOW): SetSnapshotVersionAction =
        SetSnapshotVersionAction(
            projectActionsFactory,
            scmActions,
            scmConfig,
            snapshotVersionProvider
        )

}
