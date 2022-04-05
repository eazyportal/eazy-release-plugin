package org.eazyportal.plugin.release.gradle.action

import org.eazyportal.plugin.release.core.action.ReleaseAction
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension

interface ReleaseActionFactory<T : ReleaseAction> {

    fun create(extension: EazyReleasePluginExtension): T

}