package org.eazyportal.plugin.release.core.executor

import org.eazyportal.plugin.release.core.executor.exception.CliExecutionException
import java.io.File
import java.util.concurrent.TimeUnit

class CliCommandExecutor : CommandExecutor {

    override fun execute(workingDir: File, vararg commands: String): String {
        val process = ProcessBuilder()
            .directory(workingDir)
            .command(*commands)
            .redirectErrorStream(true)
            .start()

        val output: String
        process.inputStream.use {
            output = it.bufferedReader()
                .readText()
                .trim()
        }

        process.waitFor(30, TimeUnit.SECONDS)

        val exitValue = process.exitValue()
        if (exitValue != 0) {
            throw CliExecutionException(output)
        }

        process.destroyForcibly()

        return output
    }

}
