package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.ProjectDescriptorFactory
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.gradle.action.FinalizeSnapshotVersionActionFactory
import org.eazyportal.plugin.release.gradle.action.SetSnapshotVersionActionFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetSnapshotVersionTask @Inject constructor(
    private val projectDescriptorFactory: ProjectDescriptorFactory,
    private val setSnapshotVersionActionFactory: SetSnapshotVersionActionFactory,
    private val finalizeSnapshotVersionActionFactory: FinalizeSnapshotVersionActionFactory
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Setting SNAPSHOT version...")

        val projectDescriptor: ProjectDescriptor = projectDescriptorFactory.create(
            extension.projectActionsFactory,
            extension.scmActions,
            project.projectDir
        )

        setSnapshotVersionActionFactory.create(extension)
            .execute(projectDescriptor)

        finalizeSnapshotVersionActionFactory.create(extension)
            .execute(projectDescriptor)
    }

}
