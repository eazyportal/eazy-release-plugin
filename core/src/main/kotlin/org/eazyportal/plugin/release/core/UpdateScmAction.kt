package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.scm.ScmActions
import java.io.File

class UpdateScmAction(
    private val scmActions: ScmActions
) : ReleaseAction {

    companion object {
        const val RELEASE_BRANCH = "master"
        const val REMOTE = "origin"
    }

    var releaseBranch: String = RELEASE_BRANCH
    var remote: String = REMOTE

    override fun execute(workingDir: File) {
        scmActions.push(workingDir, remote, releaseBranch)
    }

}
