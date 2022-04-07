package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FinalizeReleaseVersionAction(
    private val scmActions: ScmActions
) : ReleaseAction {

    private companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(FinalizeReleaseVersionAction::class.java)
    }

    override fun execute(projectDescriptor: ProjectDescriptor) {
        LOGGER.info("Finalize release version...")

        val releaseVersion = projectDescriptor.rootProject.projectActions.getVersion()

        projectDescriptor.allProjects.forEach {
            scmActions.add(it.dir, *it.projectActions.scmFilesToCommit())
            scmActions.commit(it.dir, "Release version: $releaseVersion")
        }

        scmActions.tag(projectDescriptor.rootProject.dir, "-a", releaseVersion.toString(), "-m", "v$releaseVersion")
    }

}
