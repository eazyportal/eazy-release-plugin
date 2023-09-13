package org.eazyportal.plugin.release.core.ac.scm

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ListAssert
import org.eazyportal.plugin.release.core.executor.CliCommandExecutor
import org.eazyportal.plugin.release.core.scm.GitActions
import java.io.File

object GitRepositoryUtils {

    private val gitActions = GitActions(CliCommandExecutor())

    fun assertThatBranches(workingDir: File): ListAssert<String> =
        gitActions.execute(workingDir, "branch", "-a")
            .lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .let { assertThat(it) }

    fun assertThatStatusContains(workingDir: File, vararg lines: String) {
        assertThat(gitActions.execute(workingDir, "status"))
            .contains(*lines)
    }

}
