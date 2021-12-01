package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.SetSnapshotVersionAction
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetSnapshotVersionTask @Inject constructor(
    private val setSnapshotVersionAction: SetSnapshotVersionAction
) : EazyBaseTask() {

    @get:Input
    val scmConfig: Property<ScmConfig> = project.objects.property(ScmConfig::class.java)

    @TaskAction
    fun run() {
        logger.quiet("Setting version...")

        setSnapshotVersionAction.scmConfig = scmConfig.get()

        setSnapshotVersionAction.execute(project.rootDir)
    }

}
