package org.eazyportal.plugin.release.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal

open class EazyBaseTask : DefaultTask() {

    @Internal
    override fun getGroup(): String {
        return "eazy"
    }

    override fun setGroup(group: String?) {
        throw UnsupportedOperationException("Not allowed to set the group of EazyTasks.")
    }

}
