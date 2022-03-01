package org.eazyportal.plugin.release.gradle

import org.eazyportal.plugin.release.core.UpdateScmAction
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension

class UpdateScmActionFactory {

    fun create(extension: EazyReleasePluginExtension): UpdateScmAction =
        UpdateScmAction().apply {
            scmActions = extension.scmActions
            scmConfig = extension.scmConfig
        }
}
