package org.eazyportal.plugin.release.core.scm

import java.io.File

interface ScmActions {

    fun add(workingDir: File, vararg filePaths: String)

    fun checkout(workingDir: File, toRef: String)

    fun commit(workingDir: File, message: String)

    fun fetch(workingDir: File, remote: String)

    fun getCommits(workingDir: File, fromRef: String? = null, toRef: String? = null): List<String>

    fun getLastTag(workingDir: File, fromRef: String? = null): String

    fun getSubmodules(workingDir: File): List<String>

    fun getTags(workingDir: File, fromRef: String? = null): List<String>

    fun mergeNoCommit(workingDir: File, fromBranch: String)

    fun push(workingDir: File, remote: String, vararg branches: String)

    fun tag(workingDir: File, vararg commands: String)

}