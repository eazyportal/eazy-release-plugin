package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.model.ProjectDescriptorMockBuilder
import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class PrepareRepositoryForReleaseActionTest : ReleaseActionBaseTest() {

    @Mock
    private lateinit var scmActions: ScmActions

    private lateinit var underTest: PrepareRepositoryForReleaseAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_execute_withGitFlow() {
        // GIVEN
        val projectActions: ProjectActions = mock()
        val projectDescriptor: ProjectDescriptor = ProjectDescriptorMockBuilder(projectActions, workingDir).build()

        underTest = PrepareRepositoryForReleaseAction(projectDescriptor, scmActions, ScmConfig.GIT_FLOW)

        // WHEN
        whenever(scmActions.getSubmodules(workingDir))
            .thenReturn(listOf(ProjectDescriptorMockBuilder.SUBMODULE_NAME))

        // THEN
        underTest.execute()

        verify(scmActions).fetch(workingDir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(scmActions).getSubmodules(workingDir)
        verify(scmActions).checkout(workingDir.resolve(ProjectDescriptorMockBuilder.SUBMODULE_NAME), ScmConfig.GIT_FLOW.featureBranch)
        verifyNoMoreInteractions(scmActions)
    }

    @Test
    fun test_execute_withTrunkBasedFlow() {
        // GIVEN
        val projectActions: ProjectActions = mock()
        val projectDescriptor: ProjectDescriptor = ProjectDescriptorMockBuilder(projectActions, workingDir).build()

        underTest = PrepareRepositoryForReleaseAction(projectDescriptor, scmActions, ScmConfig.TRUNK_BASED_FLOW)

        // WHEN
        whenever(scmActions.getSubmodules(workingDir))
            .thenReturn(listOf(ProjectDescriptorMockBuilder.SUBMODULE_NAME))

        // THEN
        underTest.execute()

        verify(scmActions).fetch(workingDir, ScmConfig.TRUNK_BASED_FLOW.remote)
        verify(scmActions).getSubmodules(workingDir)
        verifyNoMoreInteractions(scmActions)
    }

}
