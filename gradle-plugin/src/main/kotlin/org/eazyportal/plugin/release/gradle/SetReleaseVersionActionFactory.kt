package org.eazyportal.plugin.release.gradle

import org.eazyportal.plugin.release.core.SetReleaseVersionAction
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension

class SetReleaseVersionActionFactory {

    fun create(
        projectActionsFactory: ProjectActionsFactory,
        extension: EazyReleasePluginExtension
    ): SetReleaseVersionAction = SetReleaseVersionAction(projectActionsFactory, ReleaseVersionProvider(), VersionIncrementProvider()).apply {
        conventionalCommitTypes = extension.conventionalCommitTypes
        scmActions = extension.scmActions
        scmConfig = extension.scmConfig
    }

}
