package org.eazyportal.plugin.release.gradle.project

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectLocationException
import org.eazyportal.plugin.release.core.project.exception.MissingProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.exception.MultipleProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.exception.ProjectVersionPropertyException
import org.eazyportal.plugin.release.core.version.model.Version
import java.io.File

class GradleProjectActions(
    private val workingDir: File
) : ProjectActions {

    companion object {
        const val GRADLE_PROPERTIES_FILE_NAME = "gradle.properties"
    }

    private val gradlePropertiesFile: File = run {
        if (!workingDir.exists() || workingDir.isFile) {
            throw InvalidProjectLocationException("Invalid Gradle project location: $workingDir")
        }

        return@run workingDir.resolve(GRADLE_PROPERTIES_FILE_NAME)
    }

    override fun getVersion(): Version {
        if (!gradlePropertiesFile.exists()) {
            throw InvalidProjectLocationException("'$GRADLE_PROPERTIES_FILE_NAME' file is missing in: $workingDir")
        }

        val versions = gradlePropertiesFile.readLines()
            .filter { it.isVersionLine() }
            .map { it.getVersionFromLine() }
            .map { Version.of(it) }

        return when (versions.size) {
            0 -> throw MissingProjectVersionPropertyException("The project does not have version property.")
            1 -> versions[0]
            else -> throw MultipleProjectVersionPropertyException("The project has multiple versions: $versions")
        }
    }

    override fun scmFilesToCommit(): Array<String> = arrayOf(".")

    override fun setVersion(version: Version) {
        gradlePropertiesFile.createNewFile()

        val versionLines = gradlePropertiesFile.readLines()
            .filter { it.isVersionLine() }

        when (versionLines.size) {
            0 -> throw ProjectVersionPropertyException("The project does not have version property.")
            1 -> gradlePropertiesFile.readText()
                .replace(versionLines[0], versionLines[0].getNewVersionLine(version))
                .run { gradlePropertiesFile.writeText(this) }
            else -> throw MultipleProjectVersionPropertyException("The project has multiple versions: $versionLines")
        }
    }

    private fun String.getNewVersionLine(version: Version): String =
        "version = $version"

    private fun String.getVersionFromLine(): String =
        substring(indexOf("=") + 1).trim()

    private fun String.isVersionLine(): Boolean =
        trim().let { it.startsWith("version=") || it.startsWith("version =") }

}
