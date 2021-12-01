package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import java.io.File

class UpdateScmAction(
    private val scmActions: ScmActions
) : ReleaseAction {

    var scmConfig: ScmConfig = ScmConfig.GIT_FLOW

    override fun execute(workingDir: File) {
        scmActions.push(workingDir, scmConfig.remote, scmConfig.releaseBranch, scmConfig.featureBranch)
    }

}
