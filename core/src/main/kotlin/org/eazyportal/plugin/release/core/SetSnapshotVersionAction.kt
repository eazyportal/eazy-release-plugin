package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider
import java.io.File

class SetSnapshotVersionAction(
    private val projectActions: ProjectActions,
    private val snapshotVersionProvider: SnapshotVersionProvider
) : ReleaseAction {

    lateinit var scmActions: ScmActions
    lateinit var scmConfig: ScmConfig

    override fun execute(workingDir: File) {
        if (scmConfig.releaseBranch != scmConfig.featureBranch) {
            scmActions.checkout(workingDir, scmConfig.releaseBranch)
        }

        val currentVersion = projectActions.getVersion()
        val snapshotVersion  = snapshotVersionProvider.provide(currentVersion)

        if (scmConfig.releaseBranch != scmConfig.featureBranch) {
            scmActions.checkout(workingDir, scmConfig.featureBranch)

            scmActions.mergeNoCommit(workingDir, scmConfig.releaseBranch)
        }

        projectActions.setVersion(snapshotVersion)

        scmActions.add(workingDir, *projectActions.scmFilesToCommit())
        scmActions.commit(workingDir, "New snapshot version: $snapshotVersion")
    }

}
