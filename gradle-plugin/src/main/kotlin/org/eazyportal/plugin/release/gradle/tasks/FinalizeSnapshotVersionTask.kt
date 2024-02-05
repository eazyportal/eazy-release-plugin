package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.ProjectDescriptorFactory
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.gradle.action.ActionContextFactory
import org.eazyportal.plugin.release.gradle.action.FinalizeSnapshotVersionActionFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class FinalizeSnapshotVersionTask @Inject constructor(
    private val actionContextFactory: ActionContextFactory,
    private val projectDescriptorFactory: ProjectDescriptorFactory,
    private val finalizeSnapshotVersionActionFactory: FinalizeSnapshotVersionActionFactory
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Finalizing SNAPSHOT version...")

        val projectDescriptor: ProjectDescriptor = projectDescriptorFactory.create(
            extension.projectActionsFactory,
            extension.scmActions,
            project.projectDir
        )

        val actionContext = actionContextFactory.create(project.providers)

        finalizeSnapshotVersionActionFactory.create(extension)
            .execute(projectDescriptor, actionContext)
    }

}
