package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.ProjectDescriptorFactory
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.gradle.action.FinalizeReleaseVersionActionFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class FinalizeReleaseVersionTask @Inject constructor(
    private val projectDescriptorFactory: ProjectDescriptorFactory,
    private val finalizeReleaseVersionActionFactory: FinalizeReleaseVersionActionFactory
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Finalizing release version...")

        val projectDescriptor: ProjectDescriptor = projectDescriptorFactory.create(
            extension.projectActionsFactory,
            extension.scmActions,
            project.projectDir
        )

        finalizeReleaseVersionActionFactory.create(extension)
            .execute(projectDescriptor)
    }

}
