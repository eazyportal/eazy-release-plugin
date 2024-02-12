package org.eazyportal.plugin.release.gradle.project

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectTypeException
import org.eazyportal.plugin.release.core.project.model.ProjectFile

class GradleProjectActionsFactory : ProjectActionsFactory {

    override fun create(projectFile: ProjectFile<*>): ProjectActions =
        if (GradleProjectActions.isGradleProject(projectFile)) {
            GradleProjectActions(projectFile)
        } else {
            throw InvalidProjectTypeException("Unable to identify the project type in: $projectFile")
        }

}
