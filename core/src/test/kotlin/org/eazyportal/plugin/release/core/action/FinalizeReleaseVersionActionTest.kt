package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.project.model.ProjectDescriptorMockBuilder
import org.eazyportal.plugin.release.core.scm.ScmActions
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

internal class FinalizeReleaseVersionActionTest : ReleaseActionBaseTest() {

    @Mock
    private lateinit var scmActions: ScmActions

    private lateinit var underTest: FinalizeReleaseVersionAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_execute() {
        // GIVEN
        val projectActions: ProjectActions = mock()

        val projectDescriptor: ProjectDescriptor = ProjectDescriptorMockBuilder(projectActions, workingDir).build()

        underTest = createFinalizeReleaseVersionAction(projectDescriptor)

        // WHEN
        whenever(projectActions.getVersion()).thenReturn(VersionFixtures.RELEASE_001)
        whenever(projectActions.scmFilesToCommit()).thenReturn(arrayOf(FILE_TO_COMMIT))

        // THEN
        underTest.execute()

        verify(projectActions).getVersion()
        verify(projectActions, times(2)).scmFilesToCommit()
        projectDescriptor.allProjects.forEach {
            verify(scmActions).add(it.dir, FILE_TO_COMMIT)
            verify(scmActions).commit(it.dir, "Release version: ${VersionFixtures.RELEASE_001}")
            verify(scmActions).tag(it.dir, VersionFixtures.RELEASE_001)
        }
        verifyNoMoreInteractions(projectActions, scmActions)
    }

    private fun createFinalizeReleaseVersionAction(
        projectDescriptor: ProjectDescriptor
    ): FinalizeReleaseVersionAction =
        FinalizeReleaseVersionAction(
            projectDescriptor,
            scmActions
        )

}
