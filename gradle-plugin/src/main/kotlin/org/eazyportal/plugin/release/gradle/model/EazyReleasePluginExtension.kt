package org.eazyportal.plugin.release.gradle.model

import org.eazyportal.plugin.release.core.executor.CliCommandExecutor
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.eazyportal.plugin.release.core.scm.GitActions
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.gradle.api.Action
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested

abstract class EazyReleasePluginExtension {

    @get:Input
    var conventionalCommitTypes: List<ConventionalCommitType> = ConventionalCommitType.DEFAULT_TYPES

    @get:Input
    var scmActions: ScmActions = GitActions(CliCommandExecutor())

    @get:Nested
    var scmConfig: ScmConfig = ScmConfig.GIT_FLOW

    fun scmActions(action: Action<in ScmActions>) {
        action.execute(scmActions)
    }

    fun scmConfig(action: Action<in ScmConfig>) {
        action.execute(scmConfig)
    }

}