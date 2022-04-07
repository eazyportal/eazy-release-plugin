package org.eazyportal.plugin.release.core.executor

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.executor.exception.CliExecutionException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS.WINDOWS
import org.junit.jupiter.api.io.TempDir
import java.io.File

@TestInstance(PER_CLASS)
internal class CliCommandExecutorTest {

    @TempDir
    private lateinit var workingDir: File

    private val underTest = CliCommandExecutor()

    @EnabledOnOs(WINDOWS)
    @Test
    fun test_cliExecute_windows() {
        // GIVEN
        // WHEN
        val actual = underTest.execute(workingDir, "ping", "127.0.0.1", "-n", "1")

        // THEN
        assertThat(actual).contains("Packets: Sent = 1, Received = 1, Lost = 0 (0% loss)")
    }

    @DisabledOnOs(WINDOWS)
    @Test
    fun test_cliExecute_linux() {
        // GIVEN
        // WHEN
        val actual = underTest.execute(workingDir, "ping", "127.0.0.1", "-c", "1")

        // THEN
        assertThat(actual).contains("1 packets transmitted, 1 received, 0% packet loss")
    }

    @Test
    fun test_cliExecute_shouldFail_whenCommandFails() {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { underTest.execute(workingDir, "ping") }
            .isInstanceOf(CliExecutionException::class.java)
            .hasMessageContaining("Usage: ping")
    }

}