package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig

class UpdateScmAction(
    private val projectDescriptor: ProjectDescriptor,
    private val scmActions: ScmActions,
    private val scmConfig: ScmConfig
) : ReleaseAction {

    override fun execute() {
        projectDescriptor.allProjects.forEach {
            scmActions.push(it.dir, scmConfig.remote, scmConfig.releaseBranch, scmConfig.featureBranch)
        }
    }

}
