package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.model.ProjectDescriptorMockBuilder
import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider
import org.eazyportal.plugin.release.core.version.model.VersionFixtures
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class SetSnapshotVersionActionTest : ReleaseActionBaseTest() {

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
        val projectActions: ProjectActions = mock()
        val projectDescriptor: ProjectDescriptor = ProjectDescriptorMockBuilder(projectActions, workingDir).build()

        underTest = createSetSnapshotVersionAction(ScmConfig.GIT_FLOW)

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.RELEASE_001)
        whenever(snapshotVersionProvider.provide(VersionFixtures.RELEASE_001)).thenReturn(VersionFixtures.SNAPSHOT_002)

        // THEN
        underTest.execute(projectDescriptor)

        verify(projectActions).getVersion()
        verify(snapshotVersionProvider).provide(VersionFixtures.RELEASE_001)

        projectDescriptor.allProjects.forEach {
            verify(scmActions).checkout(it.dir, ScmConfig.GIT_FLOW.featureBranch)
            verify(scmActions).mergeNoCommit(it.dir, ScmConfig.GIT_FLOW.releaseBranch)
        }

        verify(projectActions, times(2)).setVersion(VersionFixtures.SNAPSHOT_002)

        verifyNoMoreInteractions(projectActions, scmActions, snapshotVersionProvider)
    }

    @Test
    fun test_execute_withTrunkBasedFlow() {
        // GIVEN
        val projectActions: ProjectActions = mock()
        val projectDescriptor: ProjectDescriptor = ProjectDescriptorMockBuilder(projectActions, workingDir).build()

        underTest = createSetSnapshotVersionAction(ScmConfig.TRUNK_BASED_FLOW)

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.RELEASE_001)
        whenever(snapshotVersionProvider.provide(VersionFixtures.RELEASE_001)).thenReturn(VersionFixtures.SNAPSHOT_002)

        // THEN
        underTest.execute(projectDescriptor)

        verify(projectActions).getVersion()
        verify(snapshotVersionProvider).provide(VersionFixtures.RELEASE_001)
        verify(projectActions, times(2)).setVersion(VersionFixtures.SNAPSHOT_002)

        verifyNoMoreInteractions(projectActions, scmActions, snapshotVersionProvider)
    }

    private fun createSetSnapshotVersionAction(scmConfig: ScmConfig = ScmConfig.GIT_FLOW): SetSnapshotVersionAction =
        SetSnapshotVersionAction(
            scmActions,
            scmConfig,
            snapshotVersionProvider
        )

}
