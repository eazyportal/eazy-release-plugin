package org.eazyportal.plugin.release.gradle.action

import org.eazyportal.plugin.release.core.action.SetReleaseVersionAction
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension

class SetReleaseVersionActionFactory : ReleaseActionFactory<SetReleaseVersionAction> {

    override fun create(extension: EazyReleasePluginExtension): SetReleaseVersionAction =
        SetReleaseVersionAction(
            extension.conventionalCommitTypes,
            extension.projectActionsFactory,
            ReleaseVersionProvider(),
            extension.scmActions,
            extension.scmConfig,
            VersionIncrementProvider()
        )

}