package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.model.Project
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.scm.ScmActions
import java.io.File

open class ProjectDescriptorFactory {

    fun create(
        projectActionsFactory: ProjectActionsFactory,
        scmActions: ScmActions,
        workingDir: File
    ): ProjectDescriptor {
        val rootProject: Project = workingDir.toProject(projectActionsFactory)

        val subProjects: List<Project> = scmActions.getSubmodules(workingDir)
            .map { workingDir.resolve(it) }
            .map { it.toProject(projectActionsFactory) }

        return ProjectDescriptor(
            allProjects = listOf(*subProjects.toTypedArray(), rootProject),
            rootProject = rootProject,
            subProjects = subProjects
        )
    }

    private fun File.toProject(projectActionsFactory: ProjectActionsFactory): Project =
        Project(this, projectActionsFactory.create(this))

}
