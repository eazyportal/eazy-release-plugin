package org.eazyportal.plugin.release.gradle.ac.project

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.project.model.ProjectFile
import org.eazyportal.plugin.release.gradle.project.GradleProjectActions

class StubProjectActionsFactory : ProjectActionsFactory {

    override fun create(projectFile: ProjectFile<*>): ProjectActions =
        if (projectFile.resolve(StubProjectActions.VERSION_JSON_FILE_NAME).exists()) {
            StubProjectActions(projectFile)
        }
        else {
            GradleProjectActions(projectFile)
        }

}
