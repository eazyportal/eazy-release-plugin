package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.exception.ScmActionException
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider
import org.eazyportal.plugin.release.core.version.model.Version
import org.eazyportal.plugin.release.core.version.model.VersionComparator
import org.eazyportal.plugin.release.core.version.model.VersionIncrement
import org.eazyportal.plugin.release.core.version.model.VersionIncrement.NONE
import org.slf4j.LoggerFactory
import java.io.File

open class SetReleaseVersionAction(
    private val conventionalCommitTypes: List<ConventionalCommitType>,
    private val projectActionsFactory: ProjectActionsFactory,
    private val releaseVersionProvider: ReleaseVersionProvider,
    private val scmActions: ScmActions,
    private val scmConfig: ScmConfig,
    private val versionIncrementProvider: VersionIncrementProvider
) : ReleaseAction {

    companion object {
        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger(SetReleaseVersionAction::class.java)
    }

    override fun execute(workingDir: File) {
        scmActions.fetch(workingDir, scmConfig.remote)

        checkoutFeatureBranch(workingDir)

        val submodulesDir = scmActions.getSubmodules(workingDir)
            .map { workingDir.resolve(it) }
            .onEach { checkoutFeatureBranch(workingDir) }

        val allProjectsDir = listOf(*submodulesDir.toTypedArray(), workingDir)

        val releaseVersion = getReleaseVersion(allProjectsDir)

        allProjectsDir.onEach {
            val projectActions = projectActionsFactory.create(it)

            if (scmConfig.releaseBranch != scmConfig.featureBranch) {
                scmActions.checkout(it, scmConfig.releaseBranch)

                scmActions.mergeNoCommit(it, scmConfig.featureBranch)
            }

            projectActions.setVersion(releaseVersion)

            scmActions.add(it, *projectActions.scmFilesToCommit())
            scmActions.commit(it, "Release version: $releaseVersion")
        }

        scmActions.tag(workingDir, "-a", releaseVersion.toString(), "-m", "v$releaseVersion")
    }

    private fun checkoutFeatureBranch(projectDir: File) {
        if (scmConfig.releaseBranch != scmConfig.featureBranch) {
            scmActions.checkout(projectDir, scmConfig.featureBranch)
        }
    }

    private fun getReleaseVersion(projectDirs: List<File>): Version = projectDirs
        .mapNotNull {
            val projectActions = projectActionsFactory.create(it)

            val currentVersion = projectActions.getVersion()
            val versionIncrement = getVersionIncrement(it)

            if ((versionIncrement == null) || (versionIncrement == NONE)) {
                null
            }
            else {
                releaseVersionProvider.provide(currentVersion, versionIncrement)
            }
        }
        .maxWithOrNull(VersionComparator())
        ?: throw IllegalArgumentException("There are no acceptable commits.")

    private fun getVersionIncrement(workingDir: File): VersionIncrement? {
        val lastTag = try {
            scmActions.getLastTag(workingDir)
        }
        catch (exception: ScmActionException) {
            LOGGER.warn("Ignoring missing Git tag from release version calculation.")

            null
        }

        return scmActions.getCommits(workingDir, lastTag)
            .let { versionIncrementProvider.provide(it, conventionalCommitTypes) }
    }

}