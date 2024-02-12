package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.project.model.ProjectDescriptorMockBuilder
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

internal class UpdateScmActionTest : ReleaseActionBaseTest() {

    companion object {
        @JvmStatic
        fun execute() = listOf(
            Arguments.of(ScmConfig.GIT_FLOW),
            Arguments.of(ScmConfig.TRUNK_BASED_FLOW),
            Arguments.of(ScmConfig("feature-branch", "release-branch", "remote-repository"))
        )
    }

    @Mock
    private lateinit var scmActions: ScmActions

    private lateinit var underTest: UpdateScmAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @MethodSource("execute")
    @ParameterizedTest
    fun test_execute(scmConfig: ScmConfig) {
        // GIVEN
        val projectActions: ProjectActions = mock()
        val projectDescriptor: ProjectDescriptor = ProjectDescriptorMockBuilder(projectActions, workingDir).build()

        underTest = createUpdateScmAction(projectDescriptor, scmConfig)

        // WHEN
        // THEN
        underTest.execute()

        projectDescriptor.allProjects.forEach {
            verify(scmActions).push(it.dir, scmConfig.remote, scmConfig.releaseBranch, scmConfig.featureBranch)
        }

        verifyNoMoreInteractions(scmActions)
    }

    private fun createUpdateScmAction(
        projectDescriptor: ProjectDescriptor,
        scmConfig: ScmConfig
    ): UpdateScmAction =
        UpdateScmAction(
            projectDescriptor,
            scmActions,
            scmConfig
        )


}