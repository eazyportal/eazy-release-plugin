package org.eazyportal.plugin.release.gradle.project

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectLocationException
import org.eazyportal.plugin.release.core.version.model.Version
import java.io.File

class GradleProjectActions(
    private val workingDir: File
) : ProjectActions() {

    companion object {
        const val GRADLE_PROPERTIES_FILE_NAME = "gradle.properties"
    }

    private val gradlePropertiesFile = run {
        if (!workingDir.exists() || workingDir.isFile) {
            throw InvalidProjectLocationException("Invalid Gradle project location: $workingDir")
        }

        return@run workingDir.resolve(GRADLE_PROPERTIES_FILE_NAME)
    }

    override fun getVersion(): Version =
        readVersion(gradlePropertiesFile)

    override fun scmFilesToCommit(): Array<String> = arrayOf(".")

    override fun setVersion(version: Version) {
        gradlePropertiesFile.createNewFile()

        writeVersion(gradlePropertiesFile, version)
    }

    override fun String.getNewVersionLine(version: Version): String =
        "version = $version"

    override fun String.getVersionFromLine(): String =
        substring(indexOf("=") + 1).trim()

    override fun String.isVersionLine(): Boolean =
        trim().let { it.startsWith("version=") || it.startsWith("version =") }

}
