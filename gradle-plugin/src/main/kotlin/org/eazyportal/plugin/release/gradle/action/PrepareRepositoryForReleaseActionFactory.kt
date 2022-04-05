package org.eazyportal.plugin.release.gradle.action

import org.eazyportal.plugin.release.core.action.PrepareRepositoryForReleaseAction
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension

class PrepareRepositoryForReleaseActionFactory {

    fun create(extension: EazyReleasePluginExtension): PrepareRepositoryForReleaseAction =
        PrepareRepositoryForReleaseAction(
            extension.scmActions,
            extension.scmConfig
        )

}