package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.gradle.SetReleaseVersionActionFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetReleaseVersionTask @Inject constructor(
    private val setReleaseVersionActionFactory: SetReleaseVersionActionFactory
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Setting release version...")

        setReleaseVersionActionFactory.create(extension)
            .run { execute(project.rootDir) }
    }

}
