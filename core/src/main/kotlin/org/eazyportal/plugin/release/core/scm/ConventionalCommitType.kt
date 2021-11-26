package org.eazyportal.plugin.release.core.scm

import org.eazyportal.plugin.release.core.version.model.VersionIncrement

data class ConventionalCommitType(
    val aliases: List<String>,
    val versionIncrement: VersionIncrement
) {

    companion object {
        const val BREAKING_CHANGE_INDICATOR = '!'
        const val TYPE_DELIMITER = ':'

        val DEFAULT_TYPES = listOf(
            ConventionalCommitType(listOf("BREAKING CHANGE"), VersionIncrement.MAJOR),

            ConventionalCommitType(listOf("feature"), VersionIncrement.MINOR),

            ConventionalCommitType(listOf("build"), VersionIncrement.PATCH),
            ConventionalCommitType(listOf("ci"), VersionIncrement.PATCH),
            ConventionalCommitType(listOf("fix"), VersionIncrement.PATCH)
        )
    }

    init {
        if (aliases.isEmpty()) {
            throw IllegalArgumentException("Required to have at least 1 alias for each type.")
        }
    }

}
