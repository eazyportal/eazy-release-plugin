package org.eazyportal.plugin.release.core.project

import org.eazyportal.plugin.release.core.project.exception.ProjectException
import org.eazyportal.plugin.release.core.project.model.ProjectFile

interface ProjectActionsFactory {

    @Throws(ProjectException::class)
    fun create(projectFile: ProjectFile<*>): ProjectActions

}
