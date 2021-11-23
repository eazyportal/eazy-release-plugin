package org.eazyportal.plugin.release.gradle.tasks

import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class UpdateScmTask @Inject constructor() : EazyBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Updating scm...")
    }

}
