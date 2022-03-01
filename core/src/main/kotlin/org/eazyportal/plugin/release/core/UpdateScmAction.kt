package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import java.io.File

class UpdateScmAction : ReleaseAction {

    lateinit var scmActions: ScmActions
    lateinit var scmConfig: ScmConfig

    override fun execute(workingDir: File) {
        val submodulesDir = scmActions.getSubmodules(workingDir)
            .map { workingDir.resolve(it) }

        listOf(workingDir, *submodulesDir.toTypedArray()).forEach {
            scmActions.push(it, scmConfig.remote, scmConfig.releaseBranch, scmConfig.featureBranch)
        }
    }

}
