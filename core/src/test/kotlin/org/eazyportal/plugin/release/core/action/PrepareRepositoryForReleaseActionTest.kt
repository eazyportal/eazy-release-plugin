package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.project.model.ProjectDescriptorMockBuilder
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
import java.io.File

internal class PrepareRepositoryForReleaseActionTest : ReleaseActionBaseTest() {

    @Mock
    private lateinit var scmActions: ScmActions<File>

    private lateinit var underTest: PrepareRepositoryForReleaseAction<File>

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_execute_withGitFlow() {
        // GIVEN
        val projectActions: ProjectActions = mock()
        val projectDescriptor: ProjectDescriptor<File> = ProjectDescriptorMockBuilder(projectActions, workingDir)
            .build()

        underTest = PrepareRepositoryForReleaseAction(projectDescriptor, scmActions, ScmConfig.GIT_FLOW)

        // WHEN
        whenever(scmActions.getSubmodules(projectDescriptor.rootProject.dir))
            .thenReturn(listOf(ProjectDescriptorMockBuilder.SUBMODULE_NAME))

        // THEN
        underTest.execute()

        verify(scmActions).fetch(projectDescriptor.rootProject.dir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(projectDescriptor.rootProject.dir, ScmConfig.GIT_FLOW.featureBranch)
        verify(scmActions).getSubmodules(projectDescriptor.rootProject.dir)
        verify(scmActions).checkout(projectDescriptor.rootProject.dir.resolve(ProjectDescriptorMockBuilder.SUBMODULE_NAME), ScmConfig.GIT_FLOW.featureBranch)
        verifyNoMoreInteractions(scmActions)
    }

    @Test
    fun test_execute_withTrunkBasedFlow() {
        // GIVEN
        val projectActions: ProjectActions = mock()
        val projectDescriptor: ProjectDescriptor<File> = ProjectDescriptorMockBuilder(projectActions, workingDir).build()

        underTest = PrepareRepositoryForReleaseAction(projectDescriptor, scmActions, ScmConfig.TRUNK_BASED_FLOW)

        // WHEN
        whenever(scmActions.getSubmodules(projectDescriptor.rootProject.dir))
            .thenReturn(listOf(ProjectDescriptorMockBuilder.SUBMODULE_NAME))

        // THEN
        underTest.execute()

        verify(scmActions).fetch(projectDescriptor.rootProject.dir, ScmConfig.TRUNK_BASED_FLOW.remote)
        verify(scmActions).getSubmodules(projectDescriptor.rootProject.dir)
        verifyNoMoreInteractions(scmActions)
    }

}
