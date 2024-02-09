package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction
import org.eazyportal.plugin.release.gradle.action.ReleaseActionFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class FinalizeSnapshotVersionTask @Inject constructor(
    private val releaseActionFactory: ReleaseActionFactory
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Finalizing SNAPSHOT version...")

        releaseActionFactory.create<FinalizeSnapshotVersionAction>(project)
            .execute()
    }

}
