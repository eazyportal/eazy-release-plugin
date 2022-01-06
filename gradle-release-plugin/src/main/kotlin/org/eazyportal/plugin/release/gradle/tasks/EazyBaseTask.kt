package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal

open class EazyBaseTask : DefaultTask() {

    @get:Input
    val scmActions: Property<ScmActions> = project.objects.property(ScmActions::class.java)
    @get:Input
    val scmConfig: Property<ScmConfig> = project.objects.property(ScmConfig::class.java)

    @Internal
    override fun getGroup(): String {
        return "eazy"
    }

    override fun setGroup(group: String?) {
        throw UnsupportedOperationException("Not allowed to set the group of EazyTasks.")
    }

}
