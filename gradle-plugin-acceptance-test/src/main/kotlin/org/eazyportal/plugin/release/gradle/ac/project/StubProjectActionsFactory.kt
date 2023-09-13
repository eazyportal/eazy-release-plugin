package org.eazyportal.plugin.release.gradle.ac.project

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.gradle.project.GradleProjectActions
import java.io.File

class StubProjectActionsFactory : ProjectActionsFactory {

    override fun create(workingDir: File): ProjectActions =
        if (workingDir.resolve(StubProjectActions.VERSION_JSON_FILE_NAME).exists()) {
            StubProjectActions(workingDir)
        }
        else {
            GradleProjectActions(workingDir)
        }

}
