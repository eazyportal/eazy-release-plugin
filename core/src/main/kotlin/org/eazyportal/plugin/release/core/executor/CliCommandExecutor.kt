package org.eazyportal.plugin.release.core.executor

import org.eazyportal.plugin.release.core.executor.exception.CliExecutionException
import org.eazyportal.plugin.release.core.project.model.ProjectFile
import java.io.File
import java.util.concurrent.TimeUnit

class CliCommandExecutor : CommandExecutor<ProjectFile<File>> {

    override fun execute(projectFile: ProjectFile<File>, vararg commands: String): String {
        val process = ProcessBuilder()
            .directory(projectFile.getFile())
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
