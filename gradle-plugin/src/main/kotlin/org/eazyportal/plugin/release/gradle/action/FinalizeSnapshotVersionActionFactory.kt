package org.eazyportal.plugin.release.gradle.action

import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension

class FinalizeSnapshotVersionActionFactory : ReleaseActionFactory<FinalizeSnapshotVersionAction> {

    override fun create(extension: EazyReleasePluginExtension): FinalizeSnapshotVersionAction =
        FinalizeSnapshotVersionAction(extension.projectActionsFactory, extension.scmActions)

}