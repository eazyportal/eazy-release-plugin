package org.eazyportal.plugin.release.gradle.project

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectLocationException
import org.eazyportal.plugin.release.core.project.exception.MissingProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.exception.MultipleProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.exception.ProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.model.ProjectFile
import org.eazyportal.plugin.release.core.version.model.Version

class GradleProjectActions<T>(
    private val projectFile: ProjectFile<T>
) : ProjectActions {

    private val gradlePropertiesFile: ProjectFile<*>

    init {
        if (!projectFile.exists() || projectFile.isFile()) {
            throw InvalidProjectLocationException("Invalid Gradle project location: $projectFile")
        }

        gradlePropertiesFile = projectFile.resolve(GRADLE_PROPERTIES_FILE_NAME)
    }

    override fun getVersion(): Version {
        if (!gradlePropertiesFile.exists()) {
            throw InvalidProjectLocationException("'$GRADLE_PROPERTIES_FILE_NAME' file is missing in: $projectFile")
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
        gradlePropertiesFile.createIfMissing()

        val versionLines = gradlePropertiesFile.readLines()
            .filter { it.isVersionLine() }

        when (versionLines.size) {
            0 -> throw ProjectVersionPropertyException("The project does not have version property.")
            1 -> gradlePropertiesFile.readText()
                .replace(versionLines[0], getNewVersionLine(version))
                .run { gradlePropertiesFile.writeText(this) }
            else -> throw MultipleProjectVersionPropertyException("The project has multiple versions: $versionLines")
        }
    }

    private fun getNewVersionLine(version: Version): String =
        "version = $version"

    private fun String.getVersionFromLine(): String =
        substring(indexOf("=") + 1).trim()

    private fun String.isVersionLine(): Boolean =
        trim().let { it.startsWith("version=") || it.startsWith("version =") }

    companion object {
        val GRADLE_PROJECT_FILES = listOf(
            "build.gradle",
            "build.gradle.kts",
            "settings.gradle",
            "settings.gradle.kts"
        )
        const val GRADLE_PROPERTIES_FILE_NAME = "gradle.properties"

        fun isGradleProject(projectFile: ProjectFile<*>): Boolean =
            GRADLE_PROJECT_FILES.any { projectFile.resolve(it).exists() }
    }

}
