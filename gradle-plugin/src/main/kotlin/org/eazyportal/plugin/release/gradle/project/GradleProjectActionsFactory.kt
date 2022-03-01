package org.eazyportal.plugin.release.gradle.project

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectTypeException
import java.io.File
import java.nio.file.Path

class GradleProjectActionsFactory: ProjectActionsFactory {

    override fun create(workingDir: File): ProjectActions {
        if (isGradleProject(workingDir.toPath())) {
            return GradleProjectActions(workingDir)
        }

        throw InvalidProjectTypeException("Unable to identify the project type.")
    }

}

fun isGradleProject(workingDirPath: Path) =
    workingDirPath.resolve(GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME).toFile().exists() ||
    workingDirPath.resolve("build.gradle").toFile().exists() ||
    workingDirPath.resolve("build.gradle.kts").toFile().exists()
