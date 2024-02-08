package org.eazyportal.plugin.release.core.executor

import org.eazyportal.plugin.release.core.executor.exception.CliExecutionException
import java.io.File

interface CommandExecutor {

    @Throws(CliExecutionException::class)
    fun execute(workingDir: File, vararg commands: String): String

}
