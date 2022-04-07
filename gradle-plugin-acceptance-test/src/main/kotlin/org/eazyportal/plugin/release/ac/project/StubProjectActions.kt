package org.eazyportal.plugin.release.ac.project

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.version.model.Version
import java.io.File

class StubProjectActions(
    private val workingDir: File
) : ProjectActions() {

    companion object {
        const val VERSION_JSON_FILE_NAME = "version.json"

        @JvmStatic
        private val VERSION_REGEX = Regex(""".*"version".?:.?"(.*?)".*""")
    }

    private val versionJsonFile = workingDir.resolve(VERSION_JSON_FILE_NAME)

    override fun getVersion(): Version =
        readVersion(versionJsonFile)

    override fun scmFilesToCommit(): Array<String> = arrayOf(".")

    override fun setVersion(version: Version) =
        writeVersion(versionJsonFile, version)

    override fun String.getNewVersionLine(version: Version): String =
        replace(this, """  "version": "$version"""")

    override fun String.getVersionFromLine(): String =
        replace(VERSION_REGEX, "$1")

    override fun String.isVersionLine(): Boolean =
        contains("version")

}
