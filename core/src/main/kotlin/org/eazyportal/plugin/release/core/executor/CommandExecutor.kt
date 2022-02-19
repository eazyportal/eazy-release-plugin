package org.eazyportal.plugin.release.core.executor

import java.io.File

interface CommandExecutor {

    fun execute(workingDir: File, vararg commands: String): String

}
