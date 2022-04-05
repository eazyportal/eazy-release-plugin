package org.eazyportal.plugin.release.gradle.action

import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension

class SetSnapshotVersionActionFactory : ReleaseActionFactory<SetSnapshotVersionAction> {

    override fun create(extension: EazyReleasePluginExtension): SetSnapshotVersionAction =
        SetSnapshotVersionAction(
            extension.projectActionsFactory,
            extension.scmActions,
            extension.scmConfig,
            SnapshotVersionProvider()
        )

}