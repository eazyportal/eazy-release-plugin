package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.project.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig

class UpdateScmAction<T>(
    private val projectDescriptor: ProjectDescriptor<T>,
    private val scmActions: ScmActions<T>,
    private val scmConfig: ScmConfig
) : ReleaseAction {

    override fun execute() {
        projectDescriptor.allProjects.forEach {
            scmActions.push(it.dir, scmConfig.remote, scmConfig.releaseBranch, scmConfig.featureBranch)
        }
    }

}
