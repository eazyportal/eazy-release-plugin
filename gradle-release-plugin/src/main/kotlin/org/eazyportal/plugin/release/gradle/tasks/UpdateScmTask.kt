package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.UpdateScmAction
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class UpdateScmTask @Inject constructor(
    private val updateScmAction: UpdateScmAction
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Updating scm...")

        updateScmAction.also {
            it.scmActions = scmActions.get()
            it.scmConfig = scmConfig.get()
        }

        updateScmAction.execute(project.rootDir)
    }

}
