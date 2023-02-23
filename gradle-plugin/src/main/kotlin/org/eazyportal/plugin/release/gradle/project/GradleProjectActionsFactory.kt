package org.eazyportal.plugin.release.gradle.project

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectTypeException
import java.io.File

class GradleProjectActionsFactory : ProjectActionsFactory {

    override fun create(workingDir: File): ProjectActions =
        if (workingDir.isGradleProjectDir()) {
            GradleProjectActions(workingDir)
        }
        else {

            throw InvalidProjectTypeException("Unable to identify the project type.")
        }

}

fun File.isGradleProjectDir(): Boolean =
    resolve("build.gradle").exists() ||
    resolve("build.gradle.kts").exists() ||
    resolve("settings.gradle").exists() ||
    resolve("settings.gradle.kts").exists()
