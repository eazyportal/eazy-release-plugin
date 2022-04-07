package org.eazyportal.plugin.release.core.scm

import org.eazyportal.plugin.release.core.version.model.VersionIncrement

data class ConventionalCommitType(
    val aliases: List<String>,
    val versionIncrement: VersionIncrement
) {

    companion object {
        const val BREAKING_CHANGE_INDICATOR = '!'
        const val TYPE_DELIMITER = ':'

        @JvmStatic
        val COMMIT_TYPE_REGEX = Regex("^(?<type>\\w*+)(?:\\((?<scope>.*?)\\))?(?:\\[(?<ticket>.*?)\\])?$")
        @JvmStatic
        val DEFAULT_TYPES = listOf(
            ConventionalCommitType(listOf("BREAKING CHANGE"), VersionIncrement.MAJOR),

            ConventionalCommitType(listOf("feature"), VersionIncrement.MINOR), // implement new feature

            ConventionalCommitType(listOf("fix"), VersionIncrement.PATCH), // add bugfix

            ConventionalCommitType(listOf("build"), VersionIncrement.NONE), // change build system
            ConventionalCommitType(listOf("chore"), VersionIncrement.NONE), // does not change src or test
            ConventionalCommitType(listOf("docs"), VersionIncrement.NONE), // add/update documentation
            ConventionalCommitType(listOf("ci"), VersionIncrement.NONE), // CI
            ConventionalCommitType(listOf("refactor"), VersionIncrement.NONE), // neither fix nor add new feature
            ConventionalCommitType(listOf("style"), VersionIncrement.NONE), // code formatting
            ConventionalCommitType(listOf("test"), VersionIncrement.NONE) // add/fix/update test
        )
    }

    init {
        if (aliases.isEmpty()) {
            throw IllegalArgumentException("Required to have at least 1 alias for each type.")
        }
    }

}
