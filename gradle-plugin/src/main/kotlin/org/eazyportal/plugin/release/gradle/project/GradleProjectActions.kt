package org.eazyportal.plugin.release.gradle.project

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectLocationException
import org.eazyportal.plugin.release.core.project.exception.MissingProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.exception.MultipleProjectVersionPropertyException
import org.eazyportal.plugin.release.core.version.model.Version
import java.io.File

class GradleProjectActions(
    private val workingDir: File
) : ProjectActions {

    companion object {
        const val GRADLE_PROPERTIES_FILE_NAME = "gradle.properties"

        private fun String.isVersionLine(): Boolean =
            trim().startsWith("version=") || trim().startsWith("version =")
    }

    private val gradlePropertiesFile = run {
        if (!workingDir.exists() || workingDir.isFile) {
            throw InvalidProjectLocationException("Invalid Gradle project location: $workingDir")
        }

        return@run workingDir.resolve(GRADLE_PROPERTIES_FILE_NAME)
    }

    override fun getVersion(): Version {
        if (!gradlePropertiesFile.exists()) {
            throw InvalidProjectLocationException("'$GRADLE_PROPERTIES_FILE_NAME' file is missing: $gradlePropertiesFile")
        }

        val versions = gradlePropertiesFile.readLines()
            .filter { it.isVersionLine() }
            .map { it.substring(it.indexOf("=") + 1).trim() }
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

        val currentVersionLines = gradlePropertiesFile.readLines()
            .filter { it.isVersionLine() }

        when (currentVersionLines.size) {
            0 -> gradlePropertiesFile.appendText("${System.lineSeparator()}version = $version${System.lineSeparator()}")
            1 -> {
                val newContent = gradlePropertiesFile.readText()
                    .replace(currentVersionLines[0], "version = $version")

                gradlePropertiesFile.writeText(newContent)
            }
            else -> throw MultipleProjectVersionPropertyException("The project has multiple versions: $currentVersionLines")
        }
    }

}
