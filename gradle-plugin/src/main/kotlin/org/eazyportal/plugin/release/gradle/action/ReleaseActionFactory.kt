package org.eazyportal.plugin.release.gradle.action

import org.eazyportal.plugin.release.core.ProjectDescriptorFactory
import org.eazyportal.plugin.release.core.action.FinalizeReleaseVersionAction
import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction
import org.eazyportal.plugin.release.core.action.ReleaseAction
import org.eazyportal.plugin.release.core.action.SetReleaseVersionAction
import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction
import org.eazyportal.plugin.release.core.action.UpdateScmAction
import org.eazyportal.plugin.release.core.model.ProjectDescriptor
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension
import org.gradle.api.Project

class ReleaseActionFactory(
    val actionContextFactory: ActionContextFactory,
    val projectDescriptorFactory: ProjectDescriptorFactory
) {

    inline fun <reified T : ReleaseAction> create(project: Project): T {
        val actionContext = actionContextFactory.create(project.providers)

        val extension = project.extensions.getByType(EazyReleasePluginExtension::class.java)

        val projectDescriptor: ProjectDescriptor = projectDescriptorFactory.create(
            extension.projectActionsFactory,
            extension.scmActions,
            project.projectDir
        )

        return when (T::class) {
            FinalizeReleaseVersionAction::class -> FinalizeReleaseVersionAction(
                projectDescriptor,
                extension.scmActions
            )

            FinalizeSnapshotVersionAction::class -> FinalizeSnapshotVersionAction(
                projectDescriptor,
                extension.scmActions
            )

            SetReleaseVersionAction::class -> SetReleaseVersionAction(
                actionContext,
                extension.conventionalCommitTypes,
                ReleaseVersionProvider(),
                projectDescriptor,
                extension.scmActions,
                extension.scmConfig,
                VersionIncrementProvider()
            )

            SetSnapshotVersionAction::class -> SetSnapshotVersionAction(
                projectDescriptor,
                extension.scmActions,
                extension.scmConfig,
                SnapshotVersionProvider()
            )

            UpdateScmAction::class -> UpdateScmAction(
                projectDescriptor,
                extension.scmActions,
                extension.scmConfig
            )

            else -> throw IllegalArgumentException("Invalid ReleaseAction implementation: ${T::class}")
        } as T
    }

}
