package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.gradle.UpdateScmActionFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class UpdateScmTask @Inject constructor(
    private val updateScmActionFactory: UpdateScmActionFactory
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Updating scm...")

        updateScmActionFactory.create(extension)
            .run { execute(project.rootDir) }
    }

}
