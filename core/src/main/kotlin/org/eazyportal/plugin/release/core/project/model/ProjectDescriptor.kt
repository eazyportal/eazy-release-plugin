package org.eazyportal.plugin.release.core.project.model

data class ProjectDescriptor<T>(
    val rootProject: Project<T>,
    val subProjects: List<Project<T>>,
    val allProjects: List<Project<T>>
)
