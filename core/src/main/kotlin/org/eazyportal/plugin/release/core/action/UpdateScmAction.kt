package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import java.io.File

class UpdateScmAction(
    private val scmActions: ScmActions,
    private val scmConfig: ScmConfig
) : ReleaseAction {

    override fun execute(workingDir: File) {
        val submodulesDir = scmActions.getSubmodules(workingDir)
            .map { workingDir.resolve(it) }

        listOf(workingDir, *submodulesDir.toTypedArray()).forEach {
            scmActions.push(it, scmConfig.remote, scmConfig.releaseBranch, scmConfig.featureBranch)
        }
    }

}