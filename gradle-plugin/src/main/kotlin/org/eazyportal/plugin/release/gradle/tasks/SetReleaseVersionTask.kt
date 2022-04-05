package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.gradle.action.FinalizeReleaseVersionActionFactory
import org.eazyportal.plugin.release.gradle.action.PrepareRepositoryForReleaseActionFactory
import org.eazyportal.plugin.release.gradle.action.SetReleaseVersionActionFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetReleaseVersionTask @Inject constructor(
    private val prepareRepositoryForReleaseActionFactory: PrepareRepositoryForReleaseActionFactory,
    private val setReleaseVersionActionFactory: SetReleaseVersionActionFactory,
    private val finalizeReleaseVersionActionFactory: FinalizeReleaseVersionActionFactory
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Setting release version...")

        prepareRepositoryForReleaseActionFactory.create(extension)
            .execute(project.projectDir)

        setReleaseVersionActionFactory.create(extension)
            .execute(project.projectDir)

        finalizeReleaseVersionActionFactory.create(extension)
            .execute(project.projectDir)
    }

}
