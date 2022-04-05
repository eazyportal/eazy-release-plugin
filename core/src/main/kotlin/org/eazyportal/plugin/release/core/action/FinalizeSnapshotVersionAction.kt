package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class FinalizeSnapshotVersionAction(
    private val projectActionsFactory: ProjectActionsFactory,
    private val scmActions: ScmActions
) : ReleaseAction {

    private companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(FinalizeSnapshotVersionAction::class.java)
    }

    override fun execute(workingDir: File) {
        LOGGER.info("Finalizing snapshot version...")

        val snapshotVersion = projectActionsFactory.create(workingDir)
            .getVersion()

        val submodulesDir = scmActions.getSubmodules(workingDir)
            .map { workingDir.resolve(it) }

        listOf(*submodulesDir.toTypedArray(), workingDir).forEach {
            scmActions.add(it, *projectActionsFactory.create(it).scmFilesToCommit())
            scmActions.commit(it, "New snapshot version: $snapshotVersion")
        }
    }

}