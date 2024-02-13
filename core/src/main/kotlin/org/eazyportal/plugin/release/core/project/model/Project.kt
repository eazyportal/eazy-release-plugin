package org.eazyportal.plugin.release.core.project.model

import org.eazyportal.plugin.release.core.project.ProjectActions

data class Project<T>(
    val dir: ProjectFile<T>,
    val projectActions: ProjectActions
)
