package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.UpdateScmAction
import org.eazyportal.plugin.release.gradle.action.ReleaseActionFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class UpdateScmTask @Inject constructor(
    private val releaseActionFactory: ReleaseActionFactory
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Updating scm...")

        releaseActionFactory.create<UpdateScmAction>(project)
            .execute()
    }

}
