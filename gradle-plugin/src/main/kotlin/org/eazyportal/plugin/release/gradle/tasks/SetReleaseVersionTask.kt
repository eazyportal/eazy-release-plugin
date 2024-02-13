package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.action.SetReleaseVersionAction
import org.eazyportal.plugin.release.gradle.action.ReleaseActionFactory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class SetReleaseVersionTask @Inject constructor(
    private val releaseActionFactory: ReleaseActionFactory
) : EazyReleaseBaseTask() {

    @TaskAction
    fun run() {
        logger.quiet("Setting release version...")

        releaseActionFactory.create<SetReleaseVersionAction<File>>(project)
            .execute()
    }

}
