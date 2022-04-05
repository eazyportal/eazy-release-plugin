package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.io.File

internal class UpdateScmActionTest {

    companion object {
        @JvmStatic
        fun execute() = listOf(
            Arguments.of(ScmConfig.GIT_FLOW),
            Arguments.of(ScmConfig.TRUNK_BASED_FLOW),
            Arguments.of(ScmConfig("feature-branch", "release-branch", "remote-repository"))
        )
    }

    @TempDir
    private lateinit var workingDir: File

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
        underTest = UpdateScmAction(scmActions, scmConfig)

        // WHEN
        // THEN
        underTest.execute(workingDir)

        verify(scmActions).getSubmodules(workingDir)
        verify(scmActions).push(workingDir, scmConfig.remote, scmConfig.releaseBranch, scmConfig.featureBranch)
        verifyNoMoreInteractions(scmActions)
    }

}