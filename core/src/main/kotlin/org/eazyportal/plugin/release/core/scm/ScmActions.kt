package org.eazyportal.plugin.release.core.scm

import org.eazyportal.plugin.release.core.version.model.Version
import java.io.File

interface ScmActions {

    fun add(workingDir: File, vararg filePaths: String)

    fun checkout(workingDir: File, toRef: String)

    fun commit(workingDir: File, message: String)

    fun fetch(workingDir: File, remote: String)

    fun getCommits(workingDir: File, fromRef: String? = null, toRef: String = "HEAD"): List<String>

    fun getLastTag(workingDir: File, fromRef: String = "HEAD"): String

    fun getSubmodules(workingDir: File): List<String>

    fun getTags(workingDir: File, fromRef: String = "HEAD"): List<String>

    fun mergeNoCommit(workingDir: File, fromBranch: String)

    fun push(workingDir: File, remote: String, vararg branches: String)

//    fun status(workingDir: File): List<String>
//
    fun tag(workingDir: File, version: Version)

}