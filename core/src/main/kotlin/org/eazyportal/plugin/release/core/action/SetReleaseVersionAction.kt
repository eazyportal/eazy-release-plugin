package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.action.model.ActionContext
import org.eazyportal.plugin.release.core.model.Project
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
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
    private val releaseVersionProvider: ReleaseVersionProvider,
    private val scmActions: ScmActions,
    private val scmConfig: ScmConfig,
    private val versionIncrementProvider: VersionIncrementProvider
) : ReleaseAction {

    companion object {
        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger(SetReleaseVersionAction::class.java)
    }

    override fun execute(
        projectDescriptor: ProjectDescriptor,
        actionContext: ActionContext
    ) {
        LOGGER.info("Setting release version...")

        projectDescriptor.allProjects.run {
            val releaseVersion = getReleaseVersion()

            forEach {
                if (scmConfig.releaseBranch != scmConfig.featureBranch) {
                    scmActions.checkout(it.dir, scmConfig.releaseBranch)

                    scmActions.mergeNoCommit(it.dir, scmConfig.featureBranch)
                }

                it.projectActions.setVersion(releaseVersion)
            }
        }
    }

    private fun List<Project>.getReleaseVersion(): Version =
        mapNotNull {
            val currentVersion = it.projectActions.getVersion()
            val versionIncrement = it.dir.getVersionIncrement()

            if ((versionIncrement == null) || (versionIncrement == NONE)) {
                null
            }
            else {
                releaseVersionProvider.provide(currentVersion, versionIncrement)
            }
        }
        .maxWithOrNull(VersionComparator())
        ?: throw IllegalArgumentException("There are no acceptable commits.")

    private fun File.getVersionIncrement(): VersionIncrement? {
        val lastTag = try {
            scmActions.getLastTag(this)
        }
        catch (exception: ScmActionException) {
            LOGGER.warn("Ignoring missing Git tag from release version calculation.")

            null
        }

        return scmActions.getCommits(this, lastTag)
            .let { versionIncrementProvider.provide(it, conventionalCommitTypes) }
    }

}
