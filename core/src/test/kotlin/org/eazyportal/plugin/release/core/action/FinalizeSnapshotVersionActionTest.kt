package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.model.ProjectDescriptorMockBuilder
import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.version.model.VersionFixtures
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class FinalizeSnapshotVersionActionTest : ReleaseActionBaseTest() {

    @Mock
    private lateinit var scmActions: ScmActions

    @InjectMocks
    private lateinit var underTest: FinalizeSnapshotVersionAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_execute() {
        // GIVEN
        val projectActions: ProjectActions = mock()

        val projectDescriptor: ProjectDescriptor = ProjectDescriptorMockBuilder(projectActions, workingDir).build()

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.SNAPSHOT_002)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf(FILE_TO_COMMIT))

        // THEN
        underTest.execute(projectDescriptor)

        verify(projectActions).getVersion()
        verify(projectActions, times(2)).scmFilesToCommit()
        projectDescriptor.allProjects.forEach {
            verify(scmActions).add(it.dir, FILE_TO_COMMIT)
            verify(scmActions).commit(it.dir, "New SNAPSHOT version: ${VersionFixtures.SNAPSHOT_002}")
        }
        verifyNoMoreInteractions(projectActions, scmActions)
    }

}