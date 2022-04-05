package org.eazyportal.plugin.release.core.model

import org.eazyportal.plugin.release.core.project.ProjectActions
import java.io.File

class ProjectDescriptorMockBuilder(
    val projectActions: ProjectActions,
    val workingDir: File
) {

    var subProjectNames: List<String> = listOf(SUBMODULE_NAME)

    companion object {
        const val SUBMODULE_NAME = "ui"
    }

    fun build(): ProjectDescriptor {
        val rootProject = workingDir.toProject()

        val subProjects = subProjectNames
            .map { workingDir.resolve(it) }
            .map { it.toProject() }

        return ProjectDescriptor(
            allProjects = listOf(*subProjects.toTypedArray(), rootProject),
            rootProject = rootProject,
            subProjects = subProjects,
        )
    }

    private fun File.toProject(): Project = Project(this, projectActions)

}
