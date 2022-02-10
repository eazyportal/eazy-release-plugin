package org.eazyportal.plugin.release.core.version

import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.eazyportal.plugin.release.core.version.model.VersionIncrement
import org.slf4j.LoggerFactory

class VersionIncrementProvider {

    companion object {
        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger(VersionIncrementProvider::class.java)
    }

    fun provide(commits: List<String>, conventionalCommitTypes: List<ConventionalCommitType> = ConventionalCommitType.DEFAULT_TYPES): VersionIncrement? {
        return commits
            .mapNotNull { mapToCommitType(it) }
            .mapNotNull { mapToVersionIncrement(it, conventionalCommitTypes) }
            .minWithOrNull(compareBy { it.priority })
    }

    private fun mapToCommitType(commit: String): String? {
        val commitTypeDelimiterIndex = commit.indexOf(ConventionalCommitType.TYPE_DELIMITER)

        return if (commitTypeDelimiterIndex > 1) {
            commit.substring(0, commitTypeDelimiterIndex)
        } else {
            LOGGER.warn("Ignoring invalid commit: $commit")

            null
        }
    }

    private fun mapToVersionIncrement(commitType: String, conventionalCommitTypes: List<ConventionalCommitType>): VersionIncrement? {
        if (commitType.endsWith(ConventionalCommitType.BREAKING_CHANGE_INDICATOR)) {
            return VersionIncrement.MAJOR
        }

        return conventionalCommitTypes
            .firstOrNull { it.aliases.contains(commitType) }
            ?.versionIncrement
    }

}
