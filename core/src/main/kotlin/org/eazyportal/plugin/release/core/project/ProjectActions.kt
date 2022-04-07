package org.eazyportal.plugin.release.core.project

import org.eazyportal.plugin.release.core.project.exception.InvalidProjectLocationException
import org.eazyportal.plugin.release.core.project.exception.MissingProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.exception.MultipleProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.exception.ProjectVersionPropertyException
import org.eazyportal.plugin.release.core.version.model.Version
import java.io.File

abstract class ProjectActions {

    abstract fun getVersion(): Version

    abstract fun scmFilesToCommit(): Array<String>

    abstract fun setVersion(version: Version)

    protected abstract fun String.getNewVersionLine(version: Version): String

    protected abstract fun String.getVersionFromLine(): String

    protected abstract fun String.isVersionLine(): Boolean

    protected fun readVersion(versionFile: File): Version {
        if (!versionFile.exists()) {
            throw InvalidProjectLocationException("'${versionFile.name}' file is missing in: ${versionFile.parent}")
        }

        val versions = versionFile.readLines()
            .filter { it.isVersionLine() }
            .map { it.getVersionFromLine() }
            .map { Version.of(it) }

        return when (versions.size) {
            0 -> throw MissingProjectVersionPropertyException("The project does not have version property.")
            1 -> versions[0]
            else -> throw MultipleProjectVersionPropertyException("The project has multiple versions: $versions")
        }
    }

    protected fun writeVersion(versionFile: File, version: Version) {
        val versionLines = versionFile.readLines()
            .filter { it.isVersionLine() }

        when (versionLines.size) {
            0 -> throw ProjectVersionPropertyException("The project does not have version property.")
            1 -> {
                versionFile.writeText(
                    versionFile.readText()
                        .replace(versionLines[0], versionLines[0].getNewVersionLine(version))
                )
            }
            else -> throw MultipleProjectVersionPropertyException("The project has multiple versions: $versionLines")
        }
    }

}
