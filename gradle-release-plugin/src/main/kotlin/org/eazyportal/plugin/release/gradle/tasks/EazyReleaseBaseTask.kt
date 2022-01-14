package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal

open class EazyReleaseBaseTask : DefaultTask() {

    companion object {
        const val GROUP = "eazy-release"
    }

    @get:Input
    val scmActions: Property<ScmActions> = project.objects.property(ScmActions::class.java)
    @get:Input
    val scmConfig: Property<ScmConfig> = project.objects.property(ScmConfig::class.java)

    @Internal
    final override fun getGroup(): String {
        return GROUP
    }

    final override fun setGroup(group: String?) {
        throw UnsupportedOperationException("Not allowed to set the group of an $GROUP task.")
    }

}
