package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.SetReleaseVersionAction
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetReleaseVersionTask @Inject constructor(
    private val setReleaseVersionAction: SetReleaseVersionAction
) : EazyBaseTask() {

    @get:Input
    lateinit var conventionalCommitTypes: ListProperty<ConventionalCommitType>

    @TaskAction
    fun run() {
        logger.quiet("Setting version...")

        setReleaseVersionAction.conventionalCommitTypes = conventionalCommitTypes
            .getOrElse(listOf())
            .ifEmpty { ConventionalCommitType.DEFAULT_TYPES }

        setReleaseVersionAction.execute(project.rootDir)
    }

}
