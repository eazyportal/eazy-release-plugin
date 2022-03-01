package org.eazyportal.plugin.release.core.project

import org.eazyportal.plugin.release.core.project.exception.InvalidProjectTypeException
import java.io.File

interface ProjectActionsFactory {

    @Throws(InvalidProjectTypeException::class)
    fun create(workingDir: File): ProjectActions

}
