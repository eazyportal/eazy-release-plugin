package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class PrepareRepositoryForReleaseAction(
    private val scmActions: ScmActions,
    private val scmConfig: ScmConfig
) {

    private companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(PrepareRepositoryForReleaseAction::class.java)
    }

    fun execute(workingDir: File) {
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

}
