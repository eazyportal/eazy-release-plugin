package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension
import org.eazyportal.plugin.release.gradle.project.GradleProjectActionsFactory
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal

open class EazyReleaseBaseTask : DefaultTask() {

    companion object {
        const val GROUP = "eazy-release"
    }

    val extension: EazyReleasePluginExtension
        @Input get() = project.extensions.getByType(EazyReleasePluginExtension::class.java)

    val projectActionsFactory: ProjectActionsFactory
        @Input get() = project.extensions.getByType(ExtraPropertiesExtension::class.java).let {
            if (it.has(EazyReleasePlugin.PROJECT_ACTIONS_FACTORY_EXTRA_PROPERTY)) {
                it.get(EazyReleasePlugin.PROJECT_ACTIONS_FACTORY_EXTRA_PROPERTY) as ProjectActionsFactory
            }
            else {
                GradleProjectActionsFactory()
            }
        }

    @Internal
    final override fun getGroup(): String {
        return GROUP
    }

    final override fun setGroup(group: String?) {
        throw UnsupportedOperationException("Not allowed to set the group of an $GROUP task.")
    }

}
