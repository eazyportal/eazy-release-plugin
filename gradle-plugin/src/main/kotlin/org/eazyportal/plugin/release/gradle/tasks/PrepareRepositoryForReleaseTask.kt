package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.PrepareRepositoryForReleaseAction
import org.eazyportal.plugin.release.gradle.action.ReleaseActionFactory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class PrepareRepositoryForReleaseTask @Inject constructor(
    private val releaseActionFactory: ReleaseActionFactory
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Preparing repository for release...")

        releaseActionFactory.create<PrepareRepositoryForReleaseAction<File>>(project)
            .execute()
    }

}
