package org.eazyportal.plugin.release.gradle.model

import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.gradle.api.Action
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested

abstract class EazyReleasePluginExtension {

    @get:Input
    var conventionalCommitTypes: List<ConventionalCommitType> = ConventionalCommitType.DEFAULT_TYPES

    @get:Nested
    var scm: ScmConfiguration = ScmConfiguration()

    fun scm(action: Action<in ScmConfiguration>) {
        action.execute(scm)
    }

}