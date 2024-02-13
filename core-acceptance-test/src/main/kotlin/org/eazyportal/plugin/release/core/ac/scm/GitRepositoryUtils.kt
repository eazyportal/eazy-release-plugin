package org.eazyportal.plugin.release.core.ac.scm

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ListAssert
import org.eazyportal.plugin.release.core.executor.CliCommandExecutor
import org.eazyportal.plugin.release.core.project.model.FileSystemProjectFile
import org.eazyportal.plugin.release.core.scm.GitActions

object GitRepositoryUtils {

    private val gitActions = GitActions(CliCommandExecutor())

    fun assertThatBranches(projectFile: FileSystemProjectFile): ListAssert<String> =
        gitActions.execute(projectFile, "branch", "-a")
            .lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .let { assertThat(it) }

    fun assertThatStatusContains(projectFile: FileSystemProjectFile, vararg lines: String) {
        assertThat(gitActions.execute(projectFile, "status"))
            .contains(*lines)
    }

}
