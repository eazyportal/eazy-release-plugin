package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.project.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.project.model.ProjectFile
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PrepareRepositoryForReleaseAction<T>(
    private val projectDescriptor: ProjectDescriptor<T>,
    private val scmActions: ScmActions<T>,
    private val scmConfig: ScmConfig
) : ReleaseAction {

    override fun execute() {
        LOGGER.info("Preparing repository for release...")

        projectDescriptor.rootProject.dir.run {
            scmActions.fetch(this, scmConfig.remote)

            checkoutFeatureBranch(this)
        }

        projectDescriptor.subProjects
            .forEach { checkoutFeatureBranch(it.dir) }
    }

    private fun checkoutFeatureBranch(projectDir: ProjectFile<T>) {
        if (scmConfig.releaseBranch != scmConfig.featureBranch) {
            scmActions.checkout(projectDir, scmConfig.featureBranch)
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(PrepareRepositoryForReleaseAction::class.java)
    }

}
