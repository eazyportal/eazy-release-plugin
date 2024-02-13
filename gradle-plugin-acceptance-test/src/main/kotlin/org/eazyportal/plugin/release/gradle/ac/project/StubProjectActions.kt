package org.eazyportal.plugin.release.gradle.ac.project

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.model.ProjectFile
import org.eazyportal.plugin.release.core.version.model.Version

class StubProjectActions(
    projectFile: ProjectFile<*>
) : ProjectActions {

    companion object {
        const val VERSION_JSON_FILE_NAME = "version.json"

        @JvmStatic
        private val VERSION_REGEX = Regex(""".*"version".?:.?"(.*?)".*""")
    }

    private val versionJsonFile = projectFile.resolve(VERSION_JSON_FILE_NAME)

    override fun getVersion(): Version =
        versionJsonFile.readLines()
            .first { it.contains("version") }
            .replace(VERSION_REGEX, "$1")
            .let { Version.of(it) }

    override fun scmFilesToCommit(): Array<String> = arrayOf(".")

    override fun setVersion(version: Version) {
        val versionLine = versionJsonFile.readLines()
            .first { it.contains("version") }

        versionJsonFile.readText()
            .replace(versionLine, """  "version": "$version"""")
            .run { versionJsonFile.writeText(this) }
    }

}
