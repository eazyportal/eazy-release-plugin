package org.eazyportal.plugin.release.gradle.action

import org.eazyportal.plugin.release.core.action.UpdateScmAction
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension

class UpdateScmActionFactory : ReleaseActionFactory<UpdateScmAction> {

    override fun create(extension: EazyReleasePluginExtension): UpdateScmAction =
        UpdateScmAction(
            extension.scmActions,
            extension.scmConfig
        )

}