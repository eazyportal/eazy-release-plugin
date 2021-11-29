package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.UpdateScmAction
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class UpdateScmTask @Inject constructor(
    private val updateScmAction: UpdateScmAction
) : EazyBaseTask() {

    @get:Input
    val releaseBranch: Property<String> = project.objects.property(String::class.java)
    @get:Input
    val remote: Property<String> = project.objects.property(String::class.java)

    @TaskAction
    fun run() {
        logger.quiet("Updating scm...")

        updateScmAction.also {
            it.releaseBranch = releaseBranch.get()
            it.remote = remote.get()
        }

        updateScmAction.execute(project.rootDir)
    }

}
