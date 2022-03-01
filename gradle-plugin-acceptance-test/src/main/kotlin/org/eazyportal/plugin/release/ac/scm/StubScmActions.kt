package org.eazyportal.plugin.release.ac.scm

import org.eazyportal.plugin.release.core.scm.ScmActions
import java.io.File

class StubScmActions(
    private val commits: List<String> = listOf("fix: message", "feature: message", "chore: message"),
    private val lastTag: String = "0.1.1",
    private val submodules: List<String> = listOf(),
    private val tags: List<String> = listOf("0.1.1", "stable")
) : ScmActions {

    override fun add(workingDir: File, vararg filePaths: String) = Unit

    override fun checkout(workingDir: File, toRef: String) = Unit

    override fun commit(workingDir: File, message: String) = Unit

    override fun fetch(workingDir: File, remote: String) = Unit

    override fun getCommits(workingDir: File, fromRef: String?, toRef: String?): List<String> = commits

    override fun getLastTag(workingDir: File, fromRef: String?): String = lastTag

    override fun getSubmodules(workingDir: File): List<String> = submodules

    override fun getTags(workingDir: File, fromRef: String?): List<String> = tags

    override fun mergeNoCommit(workingDir: File, fromBranch: String) = Unit

    override fun push(workingDir: File, remote: String, vararg branches: String) = Unit

    override fun tag(workingDir: File, vararg commands: String) = Unit

}