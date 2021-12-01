package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.UpdateScmAction
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class UpdateScmTask @Inject constructor(
    private val updateScmAction: UpdateScmAction
) : EazyBaseTask() {

    @get:Input
    val scmConfig: Property<ScmConfig> = project.objects.property(ScmConfig::class.java)

    @TaskAction
    fun run() {
        logger.quiet("Updating scm...")

        updateScmAction.also {
            it.scmConfig = scmConfig.get()
        }

        updateScmAction.execute(project.rootDir)
    }

}
