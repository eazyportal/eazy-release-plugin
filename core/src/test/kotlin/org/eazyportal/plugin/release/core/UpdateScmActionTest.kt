package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.scm.ScmActions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.io.File

internal class UpdateScmActionTest {

    companion object {
        private const val RELEASE_BRANCH = "release-branch"
        private const val REMOTE = "remote-repository"
    }

    private val workingDir = File("")

    @Mock
    private lateinit var scmActions: ScmActions

    @InjectMocks
    private lateinit var underTest: UpdateScmAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_execute() {
        // GIVEN
        underTest.releaseBranch = RELEASE_BRANCH
        underTest.remote = REMOTE

        // WHEN
        // THEN
        underTest.execute(workingDir)

        verify(scmActions).push(workingDir, REMOTE, RELEASE_BRANCH)
        verifyNoMoreInteractions(scmActions)
    }

}
