package org.eazyportal.plugin.release.core.model

import org.eazyportal.plugin.release.core.project.ProjectActions
import java.io.File

data class Project(
    val dir: File,
    val projectActions: ProjectActions
)
