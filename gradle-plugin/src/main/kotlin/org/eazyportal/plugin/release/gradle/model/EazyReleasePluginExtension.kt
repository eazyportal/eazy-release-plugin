package org.eazyportal.plugin.release.gradle.model

import org.eazyportal.plugin.release.core.executor.CliCommandExecutor
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.eazyportal.plugin.release.core.scm.GitActions
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.gradle.project.GradleProjectActionsFactory
import org.gradle.api.Action
import org.gradle.api.tasks.Input
import java.io.File

abstract class EazyReleasePluginExtension {

    @get:Input
    var conventionalCommitTypes: List<ConventionalCommitType> = ConventionalCommitType.DEFAULT_TYPES

    @get:Input
    var projectActionsFactory: ProjectActionsFactory = GradleProjectActionsFactory()

    @get:Input
    var releaseBuildTasks: List<String> = listOf("build", "publish")

    @get:Input
    var scmActions: ScmActions<File> = GitActions(CliCommandExecutor())

    @get:Input
    var scmConfig: ScmConfig = ScmConfig.GIT_FLOW

    fun scmActions(action: Action<in ScmActions<File>>) {
        action.execute(scmActions)
    }

    fun scmConfig(action: Action<in ScmConfig>) {
        action.execute(scmConfig)
    }

}