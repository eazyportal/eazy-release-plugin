package org.eazyportal.plugin.release.gradle

import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input

interface EazyReleasePluginExtension {

    @get:Input
    val conventionalCommitTypes: ListProperty<ConventionalCommitType>

}
