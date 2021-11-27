package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider
import java.io.File

class SetSnapshotVersionAction(
    private val projectActions: ProjectActions,
    private val snapshotVersionProvider: SnapshotVersionProvider
) : SetVersionAction {

    override fun execute(workingDir: File) {
        val currentVersion = projectActions.getVersion()

        val snapshotVersion  = snapshotVersionProvider.provide(currentVersion)

        projectActions.setVersion(snapshotVersion)
    }

}
