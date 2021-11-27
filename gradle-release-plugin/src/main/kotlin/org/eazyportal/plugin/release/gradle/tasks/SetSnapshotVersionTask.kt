package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.SetSnapshotVersionAction
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetSnapshotVersionTask @Inject constructor(
    private val setSnapshotVersionAction: SetSnapshotVersionAction
) : EazyBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Setting version...")

        setSnapshotVersionAction.execute(project.rootDir)
    }

}
