package org.eazyportal.plugin.release.core.scm

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.executor.CommandExecutor
import org.eazyportal.plugin.release.core.scm.exception.ScmActionException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.File

internal class GitActionsTest {

    companion object {
        const val COMMIT_HASH_1 = "hash-1"
        const val COMMIT_HASH_2 = "hash-2"
        val COMMIT_MESSAGES = listOf(
            "chore: commit #3",
            "fix: commit #2",
            "feature: commit #1",
            "initial commit"
        )
        const val TAG_1 = "0.1.1"
        const val TAG_2 = "0.1.2"
    }

    private val workingDir = File("")

    @Mock
    private lateinit var commandExecutor: CommandExecutor

    @InjectMocks
    private lateinit var underTest: GitActions

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_execute() {
        // GIVEN
        val response = "dummy response"

        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "log")).thenReturn(response)

        // THEN
        val actual = underTest.execute(workingDir, "log")

        assertThat(actual).isEqualTo(response)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "log")
        verifyNoMoreInteractions(commandExecutor)
    }

    @Test
    fun test_execute_shouldThrowException() {
        // GIVEN
        val errorMessage = "error message"

        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "log")).thenThrow(RuntimeException(errorMessage))

        // THEN
        assertThatThrownBy { underTest.execute(workingDir, "log") }
            .isInstanceOf(ScmActionException::class.java)
            .cause
            .hasMessage(errorMessage)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "log")
        verifyNoMoreInteractions(commandExecutor)
    }

    @Test
    fun test_getCommits() {
        // GIVEN
        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "log", "--pretty=format:%s", "HEAD"))
            .thenReturn(COMMIT_MESSAGES.joinToString(System.lineSeparator()))

        // THEN
        val actual = underTest.getCommits(workingDir)

        assertThat(actual).isEqualTo(COMMIT_MESSAGES)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "log", "--pretty=format:%s", "HEAD")
        verifyNoMoreInteractions(commandExecutor)
    }

    @Test
    fun test_getCommits_withRefs() {
        // GIVEN
        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "log", "--pretty=format:%s", "$COMMIT_HASH_1..$COMMIT_HASH_2"))
            .thenReturn(COMMIT_MESSAGES.joinToString(System.lineSeparator()))

        // THEN
        val actual = underTest.getCommits(workingDir, COMMIT_HASH_1, COMMIT_HASH_2)

        assertThat(actual).isEqualTo(COMMIT_MESSAGES)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "log", "--pretty=format:%s", "$COMMIT_HASH_1..$COMMIT_HASH_2")
        verifyNoMoreInteractions(commandExecutor)
    }

    @Test
    fun test_getLastTag() {
        // GIVEN
        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "describe", "--abbrev=0", "--tags", "HEAD"))
            .thenReturn(TAG_1)

        // THEN
        val actual = underTest.getLastTag(workingDir)

        assertThat(actual).isEqualTo(TAG_1)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "describe", "--abbrev=0", "--tags", "HEAD")
        verifyNoMoreInteractions(commandExecutor)
    }

    @Test
    fun test_getLastTag_withRef() {
        // GIVEN
        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "describe", "--abbrev=0", "--tags", COMMIT_HASH_1))
            .thenReturn(TAG_1)

        // THEN
        val actual = underTest.getLastTag(workingDir, COMMIT_HASH_1)

        assertThat(actual).isEqualTo(TAG_1)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "describe", "--abbrev=0", "--tags", COMMIT_HASH_1)
        verifyNoMoreInteractions(commandExecutor)
    }

    @Test
    fun test_getTags() {
        // GIVEN
        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "tag", "--sort=-creatordate", "--contains", "HEAD"))
            .thenReturn("$TAG_1${System.lineSeparator()}$TAG_2")

        // THEN
        val actual = underTest.getTags(workingDir)

        assertThat(actual).isEqualTo(listOf(TAG_1, TAG_2))

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "tag", "--sort=-creatordate", "--contains", "HEAD")
        verifyNoMoreInteractions(commandExecutor)
    }

    @Test
    fun test_getGitTags_withRef() {
        // GIVEN
        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "tag", "--sort=-creatordate", "--contains", COMMIT_HASH_1))
            .thenReturn("$TAG_1${System.lineSeparator()}$TAG_2")

        // THEN
        val actual = underTest.getTags(workingDir, COMMIT_HASH_1)

        assertThat(actual).isEqualTo(listOf(TAG_1, TAG_2))

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "tag", "--sort=-creatordate", "--contains", COMMIT_HASH_1)
        verifyNoMoreInteractions(commandExecutor)
    }

}
