package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.exception.ScmActionException
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider
import org.eazyportal.plugin.release.core.version.model.VersionIncrement
import org.slf4j.LoggerFactory
import java.io.File

open class SetReleaseVersionAction(
    private val projectActions: ProjectActions,
    private val releaseVersionProvider: ReleaseVersionProvider
) : ReleaseAction {

    companion object {
        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger(SetReleaseVersionAction::class.java)
    }

    lateinit var conventionalCommitTypes: List<ConventionalCommitType>
    lateinit var scmActions: ScmActions
    lateinit var scmConfig: ScmConfig

    override fun execute(workingDir: File) {
        scmActions.fetch(workingDir, scmConfig.remote)

        if (scmConfig.releaseBranch != scmConfig.featureBranch) {
            scmActions.checkout(workingDir, scmConfig.featureBranch)
        }

        val currentVersion = projectActions.getVersion()
        val versionIncrement = getVersionIncrement(workingDir)
        val releaseVersion = releaseVersionProvider.provide(currentVersion, versionIncrement)

        if (scmConfig.releaseBranch != scmConfig.featureBranch) {
            scmActions.checkout(workingDir, scmConfig.releaseBranch)

            scmActions.mergeNoCommit(workingDir, scmConfig.featureBranch)
        }

        projectActions.setVersion(releaseVersion)

        scmActions.add(workingDir, *projectActions.scmFilesToCommit())
        scmActions.commit(workingDir, "Release version: $releaseVersion")
        scmActions.tag(workingDir, "-a", releaseVersion.toString(), "-m", "v${releaseVersion}")
    }

    private fun getVersionIncrement(workingDir: File): VersionIncrement {
        val lastTag = try {
            scmActions.getLastTag(workingDir)
        }
        catch (exception: ScmActionException) {
            LOGGER.warn("Ignoring missing Git tag from release version calculation.")

            null
        }

        return scmActions.getCommits(workingDir, lastTag)
            .mapNotNull { mapToCommitType(it) }
            .mapNotNull { mapToVersionIncrement(it) }
            .ifEmpty { throw IllegalArgumentException("There are no acceptable commits since the previous release {tag: $lastTag}.") }
            .reduce { acc, versionIncrement -> acc.coerceAtMost(versionIncrement) }
    }

    private fun mapToCommitType(commit: String): String? {
        val commitTypeDelimiterIndex = commit.indexOf(ConventionalCommitType.TYPE_DELIMITER)

        return if (commitTypeDelimiterIndex > 1) {
            commit.substring(0, commitTypeDelimiterIndex)
        }
        else {
            LOGGER.warn("Ignoring invalid commit: $commit")

            null
        }
    }

    private fun mapToVersionIncrement(commitType: String): VersionIncrement? {
        if (commitType.endsWith(ConventionalCommitType.BREAKING_CHANGE_INDICATOR)) {
            return VersionIncrement.MAJOR
        }

        return conventionalCommitTypes.ifEmpty { ConventionalCommitType.DEFAULT_TYPES }
            .firstOrNull { it.aliases.contains(commitType) }
            ?.versionIncrement
    }

}
