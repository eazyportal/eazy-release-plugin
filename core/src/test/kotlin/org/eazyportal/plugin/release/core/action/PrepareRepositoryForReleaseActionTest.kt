package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

internal class PrepareRepositoryForReleaseActionTest : ReleaseActionBaseTest() {

    private companion object {
        const val SUBMODULE_NAME = "ui"
    }

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
        underTest = PrepareRepositoryForReleaseAction(scmActions, ScmConfig.GIT_FLOW)

        // WHEN
        whenever(scmActions.getSubmodules(workingDir)).thenReturn(listOf(SUBMODULE_NAME))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).fetch(workingDir, ScmConfig.GIT_FLOW.remote)
        verify(scmActions).checkout(workingDir, ScmConfig.GIT_FLOW.featureBranch)
        verify(scmActions).getSubmodules(workingDir)
        verify(scmActions).checkout(workingDir.resolve(SUBMODULE_NAME), ScmConfig.GIT_FLOW.featureBranch)
        verifyNoMoreInteractions(scmActions)
    }

    @Test
    fun test_execute_withTrunkBasedFlow() {
        // GIVEN
        underTest = PrepareRepositoryForReleaseAction(scmActions, ScmConfig.TRUNK_BASED_FLOW)

        // WHEN
        whenever(scmActions.getSubmodules(workingDir)).thenReturn(listOf(SUBMODULE_NAME))

        // THEN
        underTest.execute(workingDir)

        verify(scmActions).fetch(workingDir, ScmConfig.TRUNK_BASED_FLOW.remote)
        verify(scmActions).getSubmodules(workingDir)
        verifyNoMoreInteractions(scmActions)
    }

}
