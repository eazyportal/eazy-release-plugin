package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction
import org.eazyportal.plugin.release.gradle.action.ReleaseActionFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetSnapshotVersionTask @Inject constructor(
    private val releaseActionFactory: ReleaseActionFactory
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Setting SNAPSHOT version...")

        releaseActionFactory.create<SetSnapshotVersionAction>(project)
            .execute()
    }

}
