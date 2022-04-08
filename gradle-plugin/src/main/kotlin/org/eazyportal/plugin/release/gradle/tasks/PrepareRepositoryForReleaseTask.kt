package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.gradle.action.PrepareRepositoryForReleaseActionFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class PrepareRepositoryForReleaseTask @Inject constructor(
    private val prepareRepositoryForReleaseActionFactory: PrepareRepositoryForReleaseActionFactory,
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Preparing repository for release...")

        prepareRepositoryForReleaseActionFactory.create(extension)
            .execute(project.projectDir)
    }

}
