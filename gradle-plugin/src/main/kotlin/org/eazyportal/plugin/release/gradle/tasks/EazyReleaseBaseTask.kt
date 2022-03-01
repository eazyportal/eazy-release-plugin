package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal

open class EazyReleaseBaseTask : DefaultTask() {

    companion object {
        const val GROUP = "eazy-release"
    }

    val extension: EazyReleasePluginExtension
        @Input get() = project.extensions.getByType(EazyReleasePluginExtension::class.java)

    @Internal
    final override fun getGroup(): String {
        return GROUP
    }

    final override fun setGroup(group: String?) {
        throw UnsupportedOperationException("Not allowed to set the group of an $GROUP task.")
    }

}
