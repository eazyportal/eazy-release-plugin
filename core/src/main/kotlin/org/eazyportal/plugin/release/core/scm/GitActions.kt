package org.eazyportal.plugin.release.core.scm

import org.eazyportal.plugin.release.core.executor.CommandExecutor
import org.eazyportal.plugin.release.core.scm.exception.ScmActionException
import org.eazyportal.plugin.release.core.utils.isWindows
import java.io.File

class GitActions(
    private val commandExecutor: CommandExecutor
) : ScmActions {

    companion object {
        internal val GIT_EXECUTABLE =
            if (isWindows()) "git.exe" else "git"
    }

    override fun getCommits(workingDir: File, fromRef: String?, toRef: String?): List<String> {
        val refs = listOfNotNull(fromRef, (toRef ?: "HEAD"))
            .joinToString("..")

        return execute(workingDir, "log", "--pretty=format:%s", refs)
            .split(System.lineSeparator())
    }

    override fun getLastTag(workingDir: File, fromRef: String?): String {
        return execute(workingDir, "describe", "--abbrev=0", "--tags", (fromRef ?: "HEAD"))
    }

    override fun getTags(workingDir: File, fromRef: String?): List<String> {
        return execute(workingDir, "tag", "--sort=-creatordate", "--contains", (fromRef ?: "HEAD"))
            .split(System.lineSeparator())
    }

    internal fun execute(workingDir: File, vararg gitCommands: String): String {
        try {
            return commandExecutor.execute(workingDir, GIT_EXECUTABLE, *gitCommands)
        }
        catch (exception: Exception) {
            throw ScmActionException(exception)
        }
    }

}
