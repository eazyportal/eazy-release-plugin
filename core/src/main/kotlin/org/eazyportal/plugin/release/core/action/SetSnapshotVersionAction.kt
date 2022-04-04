package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider
import java.io.File

class SetSnapshotVersionAction(
    private val projectActionsFactory: ProjectActionsFactory,
    private val snapshotVersionProvider: SnapshotVersionProvider
) : ReleaseAction {

    lateinit var scmActions: ScmActions
    lateinit var scmConfig: ScmConfig

    override fun execute(workingDir: File) {
        val submodulesDir = scmActions.getSubmodules(workingDir)
            .map { workingDir.resolve(it) }

        listOf(*submodulesDir.toTypedArray(), workingDir).onEach {
            if (scmConfig.releaseBranch != scmConfig.featureBranch) {
                scmActions.checkout(it, scmConfig.releaseBranch)
            }

            val projectActions = projectActionsFactory.create(it)
            val currentVersion = projectActions.getVersion()
            val snapshotVersion  = snapshotVersionProvider.provide(currentVersion)

            if (scmConfig.releaseBranch != scmConfig.featureBranch) {
                scmActions.checkout(it, scmConfig.featureBranch)

                scmActions.mergeNoCommit(it, scmConfig.releaseBranch)
            }

            projectActions.setVersion(snapshotVersion)

            scmActions.add(it, *projectActions.scmFilesToCommit())
            scmActions.commit(it, "New snapshot version: $snapshotVersion")
        }
    }

}