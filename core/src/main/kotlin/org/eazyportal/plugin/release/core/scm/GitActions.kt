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

        val LINE_BREAK_REGEX = Regex("\r?\n")
    }

    override fun add(workingDir: File, vararg filePaths: String) {
        execute(workingDir, "add", *filePaths)
    }

    override fun checkout(workingDir: File, toRef: String) {
        execute(workingDir, "checkout", toRef)
    }

    override fun commit(workingDir: File, message: String) {
        execute(workingDir, "commit", "-m", message)
    }

    override fun fetch(workingDir: File, remote: String) {
        execute(workingDir, "fetch", remote)
    }

    override fun getCommits(workingDir: File, fromRef: String?, toRef: String?): List<String> {
        val refs = listOfNotNull(fromRef, (toRef ?: "HEAD"))
            .joinToString("..")

        return execute(workingDir, "log", "--pretty=format:%s", refs)
            .split(LINE_BREAK_REGEX)
    }

    override fun getLastTag(workingDir: File, fromRef: String?): String {
        return execute(workingDir, "describe", "--abbrev=0", "--tags", (fromRef ?: "HEAD"))
    }

    override fun getTags(workingDir: File, fromRef: String?): List<String> {
        return execute(workingDir, "tag", "--sort=-creatordate", "--contains", (fromRef ?: "HEAD"))
            .split(LINE_BREAK_REGEX)
    }

    override fun mergeNoCommit(workingDir: File, fromBranch: String) {
        execute(workingDir, "merge", "--no-ff", "--no-commit", "-Xtheirs", fromBranch)
    }

    override fun push(workingDir: File, remote: String, vararg branches: String) {
        val branchesRefs = branches
            .distinct()
            .map { "$it:$it" }
            .toTypedArray()

        execute(workingDir, "push", "--atomic", "--tags", remote, *branchesRefs)
    }

    override fun tag(workingDir: File, vararg commands: String) {
        execute(workingDir, "tag", *commands)
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
