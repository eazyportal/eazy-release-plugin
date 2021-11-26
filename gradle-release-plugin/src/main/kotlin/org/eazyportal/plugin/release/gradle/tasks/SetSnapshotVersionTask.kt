package org.eazyportal.plugin.release.gradle.tasks

import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetSnapshotVersionTask @Inject constructor() : EazyBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Setting version...")
    }

}
