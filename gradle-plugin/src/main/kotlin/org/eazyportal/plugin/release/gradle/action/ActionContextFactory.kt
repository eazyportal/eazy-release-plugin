package org.eazyportal.plugin.release.gradle.action

import org.eazyportal.plugin.release.core.action.model.ActionContext
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory

class ActionContextFactory {

    fun create(providerFactory: ProviderFactory): ActionContext =
        ActionContext(
            isForceRelease = providerFactory.systemProperty("forceRelease").asBoolean()
        )

    private fun Provider<String>.asBoolean(): Boolean =
        try {
            getOrElse("false")
                .takeIf { it.isNotBlank() }
                ?.toBoolean()
                ?: true
        } catch (_: Exception) {
            false
        }

}
