package org.eazyportal.plugin.release.core.executor

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.executor.exception.CliExecutionException
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.nio.file.Files

@TestInstance(PER_CLASS)
internal class CliCommandExecutorTest {

    private val workingDir = Files.createTempDirectory("")
        .toFile()

    private val underTest = CliCommandExecutor()

    @AfterAll
    fun cleanUp() {
        workingDir.delete()
    }

    @Test
    fun test_cliExecute() {
        // GIVEN
        // WHEN
        val actual = underTest.execute(workingDir, "ping", "127.0.0.1", "-n", "1")

        // THEN
        assertThat(actual).contains("Packets: Sent = 1, Received = 1, Lost = 0 (0% loss)")
    }

    @Test
    fun test_cliExecute_shouldFail_whenCommandFails() {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { underTest.execute(workingDir, "ping") }
            .isInstanceOf(CliExecutionException::class.java)
            .hasMessageContaining("Ping the specified host until stopped.") // -t
    }

}