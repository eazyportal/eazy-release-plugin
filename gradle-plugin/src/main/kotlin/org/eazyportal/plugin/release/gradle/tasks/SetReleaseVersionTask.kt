package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.SetReleaseVersionAction
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetReleaseVersionTask @Inject constructor(
    private val setReleaseVersionAction: SetReleaseVersionAction
) : EazyReleaseBaseTask() {

    @get:Input
    val conventionalCommitTypes: ListProperty<ConventionalCommitType> = project.objects.listProperty(ConventionalCommitType::class.java)

    @TaskAction
    fun run() {
        logger.quiet("Setting release version...")

        setReleaseVersionAction.also {
            it.conventionalCommitTypes = conventionalCommitTypes.get()
            it.scmActions = scmActions.get()
            it.scmConfig = scmConfig.get()
        }

        setReleaseVersionAction.execute(project.rootDir)
    }

}
