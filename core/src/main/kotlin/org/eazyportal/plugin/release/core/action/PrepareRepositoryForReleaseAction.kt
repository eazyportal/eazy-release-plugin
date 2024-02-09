package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class PrepareRepositoryForReleaseAction(
    projectDescriptor: ProjectDescriptor,
    private val scmActions: ScmActions,
    private val scmConfig: ScmConfig
) : ReleaseAction {

    private val workingDir = projectDescriptor.rootProject.dir

    override fun execute() {
        LOGGER.info("Preparing repository for release...")

        scmActions.fetch(workingDir, scmConfig.remote)

        checkoutFeatureBranch(workingDir)

        scmActions.getSubmodules(workingDir)
            .map { workingDir.resolve(it) }
            .forEach { checkoutFeatureBranch(it) }
    }

    private fun checkoutFeatureBranch(projectDir: File) {
        if (scmConfig.releaseBranch != scmConfig.featureBranch) {
            scmActions.checkout(projectDir, scmConfig.featureBranch)
        }
    }


    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(PrepareRepositoryForReleaseAction::class.java)
    }

}
