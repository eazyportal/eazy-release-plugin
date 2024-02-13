package org.eazyportal.plugin.release.core.project

import org.eazyportal.plugin.release.core.project.model.Project
import org.eazyportal.plugin.release.core.project.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.project.model.ProjectFile
import org.eazyportal.plugin.release.core.scm.ScmActions

open class ProjectDescriptorFactory<T> {

    fun create(
        projectActionsFactory: ProjectActionsFactory,
        scmActions: ScmActions<T>,
        rootProjectFile: ProjectFile<T>
    ): ProjectDescriptor<T> {
        val rootProject: Project<T> = rootProjectFile.toProject(projectActionsFactory)

        val subProjects: List<Project<T>> = scmActions.getSubmodules(rootProject.dir)
            .map { rootProject.dir.resolve(it) }
            .map { it.toProject(projectActionsFactory) }

        return ProjectDescriptor(
            allProjects = listOf(*subProjects.toTypedArray(), rootProject),
            rootProject = rootProject,
            subProjects = subProjects
        )
    }

    private fun ProjectFile<T>.toProject(projectActionsFactory: ProjectActionsFactory): Project<T> =
        Project(this, projectActionsFactory.create(this))

}
