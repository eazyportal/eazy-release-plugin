package org.eazyportal.plugin.release.core.project

import org.eazyportal.plugin.release.core.project.exception.ProjectException
import java.io.File

interface ProjectActionsFactory {

    @Throws(ProjectException::class)
    fun create(workingDir: File): ProjectActions

}
