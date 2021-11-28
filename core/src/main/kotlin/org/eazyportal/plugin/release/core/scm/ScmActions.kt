package org.eazyportal.plugin.release.core.scm

import java.io.File

interface ScmActions {

    fun add(workingDir: File, vararg filePaths: String)

    fun commit(workingDir: File, message: String)

    fun getCommits(workingDir: File, fromRef: String? = null, toRef: String? = null): List<String>

    fun getLastTag(workingDir: File, fromRef: String? = null): String

    fun getTags(workingDir: File, fromRef: String? = null): List<String>

    fun tag(workingDir: File, vararg commands: String)

}