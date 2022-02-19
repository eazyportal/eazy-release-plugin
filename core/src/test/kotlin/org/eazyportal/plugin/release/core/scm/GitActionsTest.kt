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

        const val COMMIT_MESSAGE_1 = "feature: commit"
        const val COMMIT_MESSAGE_2 = "fix: commit"
        const val COMMIT_MESSAGE_3 = "chore: commit"
        val COMMIT_MESSAGES = listOf(COMMIT_MESSAGE_1, COMMIT_MESSAGE_2, COMMIT_MESSAGE_3)

        const val TAG_1 = "0.1.1"
        const val TAG_2 = "0.1.2"
        const val TAG_3 = "0.1.3"
        val TAGS = listOf(TAG_1, TAG_2, TAG_3)
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
    fun test_add() {
        // GIVEN
        val filePaths = arrayOf(".")

        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "add", *filePaths))
            .thenReturn("")

        // THEN
        underTest.add(workingDir, *filePaths)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "add", *filePaths)
        verifyNoMoreInteractions(commandExecutor)
    }

    @Test
    fun test_commit() {
        // GIVEN
        val message = "commit message"

        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "commit", "-m", message))
            .thenReturn("")

        // THEN
        underTest.commit(workingDir, message)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "commit", "-m", message)
        verifyNoMoreInteractions(commandExecutor)
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
            .thenReturn("$COMMIT_MESSAGE_1\n$COMMIT_MESSAGE_2\r\n$COMMIT_MESSAGE_3")

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
            .thenReturn("$TAG_1\n$TAG_2\r\n$TAG_3")

        // THEN
        val actual = underTest.getTags(workingDir)

        assertThat(actual).isEqualTo(TAGS)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "tag", "--sort=-creatordate", "--contains", "HEAD")
        verifyNoMoreInteractions(commandExecutor)
    }

    @Test
    fun test_getGitTags_withRef() {
        // GIVEN
        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "tag", "--sort=-creatordate", "--contains", COMMIT_HASH_1))
            .thenReturn(TAGS.joinToString(System.lineSeparator()))

        // THEN
        val actual = underTest.getTags(workingDir, COMMIT_HASH_1)

        assertThat(actual).isEqualTo(TAGS)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "tag", "--sort=-creatordate", "--contains", COMMIT_HASH_1)
        verifyNoMoreInteractions(commandExecutor)
    }

    @Test
    fun test_push() {
        // GIVEN
        val remote = "remote-repository"
        val branch = "release-branch"

        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "push", "--atomic", "--tags", remote, "$branch:$branch"))
            .thenReturn("")

        // THEN
        underTest.push(workingDir, remote, branch)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "push", "--atomic", "--tags", remote, "$branch:$branch")
        verifyNoMoreInteractions(commandExecutor)
    }

    @Test
    fun test_tag() {
        // GIVEN
        val commands = arrayOf("0.0.1")

        // WHEN
        whenever(commandExecutor.execute(workingDir, GitActions.GIT_EXECUTABLE, "tag", *commands))
            .thenReturn("")

        // THEN
        underTest.tag(workingDir, *commands)

        verify(commandExecutor).execute(workingDir, GitActions.GIT_EXECUTABLE, "tag", *commands)
        verifyNoMoreInteractions(commandExecutor)
    }

}
