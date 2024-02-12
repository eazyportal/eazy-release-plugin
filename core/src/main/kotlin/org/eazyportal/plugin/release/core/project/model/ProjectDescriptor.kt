package org.eazyportal.plugin.release.core.project.model

data class ProjectDescriptor(
    val rootProject: Project,
    val subProjects: List<Project>,
    val allProjects: List<Project>
)
