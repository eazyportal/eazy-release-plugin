package org.eazyportal.plugin.release.gradle.action

import org.eazyportal.plugin.release.core.action.FinalizeReleaseVersionAction
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension

class FinalizeReleaseVersionActionFactory : ReleaseActionFactory<FinalizeReleaseVersionAction> {

    override fun create(extension: EazyReleasePluginExtension): FinalizeReleaseVersionAction =
        FinalizeReleaseVersionAction(extension.projectActionsFactory, extension.scmActions)

}
