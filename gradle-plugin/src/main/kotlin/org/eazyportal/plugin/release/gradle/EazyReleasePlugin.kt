package org.eazyportal.plugin.release.gradle

import org.eazyportal.plugin.release.core.ProjectDescriptorFactory
import org.eazyportal.plugin.release.gradle.action.FinalizeReleaseVersionActionFactory
import org.eazyportal.plugin.release.gradle.action.FinalizeSnapshotVersionActionFactory
import org.eazyportal.plugin.release.gradle.action.PrepareRepositoryForReleaseActionFactory
import org.eazyportal.plugin.release.gradle.action.SetReleaseVersionActionFactory
import org.eazyportal.plugin.release.gradle.action.SetSnapshotVersionActionFactory
import org.eazyportal.plugin.release.gradle.action.UpdateScmActionFactory
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension
import org.eazyportal.plugin.release.gradle.tasks.EazyReleaseBaseTask
import org.eazyportal.plugin.release.gradle.tasks.FinalizeReleaseVersionTask
import org.eazyportal.plugin.release.gradle.tasks.FinalizeSnapshotVersionTask
import org.eazyportal.plugin.release.gradle.tasks.PrepareRepositoryForReleaseTask
import org.eazyportal.plugin.release.gradle.tasks.SetReleaseVersionTask
import org.eazyportal.plugin.release.gradle.tasks.SetSnapshotVersionTask
import org.eazyportal.plugin.release.gradle.tasks.UpdateScmTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer

class EazyReleasePlugin : Plugin<Project> {

    companion object {
        const val FINALIZE_RELEASE_VERSION_TASK_NAME = "finalizeReleaseVersion"
        const val FINALIZE_SNAPSHOT_VERSION_TASK_NAME = "finalizeSnapshotVersion"
        const val PREPARE_REPOSITORY_FOR_RELEASE_TASK_NAME = "prepareRepositoryForRelease"
        const val RELEASE_BUILD_TASK_NAME = "releaseBuild"
        const val RELEASE_TASK_NAME = "release"
        const val SET_RELEASE_VERSION_TASK_NAME = "setReleaseVersion"
        const val SET_SNAPSHOT_VERSION_TASK_NAME = "setSnapshotVersion"
        const val UPDATE_SCM_TASK_NAME = "updateScm"

        const val EXTENSION_NAME = "eazyRelease"
    }

    override fun apply(project: Project) {
        project.extensions.create(EXTENSION_NAME, EazyReleasePluginExtension::class.java)

        val projectDescriptorFactory = ProjectDescriptorFactory()

        project.tasks.run {
            registerPrepareRepositoryForReleaseTask()

            registerSetReleaseVersionTask(projectDescriptorFactory).configure {
                it.mustRunAfter(PREPARE_REPOSITORY_FOR_RELEASE_TASK_NAME)
            }

            registerFinalizeReleaseVersionTask(projectDescriptorFactory).configure {
                it.mustRunAfter(SET_RELEASE_VERSION_TASK_NAME)
            }

            registerEazyReleaseBaseTask(RELEASE_BUILD_TASK_NAME).configure { task ->
                task.mustRunAfter(FINALIZE_RELEASE_VERSION_TASK_NAME)

                task.extension.releaseBuildTasks
                    .flatMap { releaseBuildTask ->
                        project.allprojects.mapNotNull {
                            it.tasks.findByName(releaseBuildTask)?.path
                        }
                    }
                    .run { task.dependsOn(this) }
            }

            registerSetSnapshotVersionTask(projectDescriptorFactory).configure {
                it.mustRunAfter(RELEASE_BUILD_TASK_NAME)
            }

            registerFinalizeSnapshotVersionTask(projectDescriptorFactory).configure {
                it.mustRunAfter(SET_SNAPSHOT_VERSION_TASK_NAME)
            }

            registerUpdateScmTask(projectDescriptorFactory).configure {
                it.mustRunAfter(FINALIZE_SNAPSHOT_VERSION_TASK_NAME)
            }

            registerEazyReleaseBaseTask(RELEASE_TASK_NAME).configure {
                it.dependsOn(
                    PREPARE_REPOSITORY_FOR_RELEASE_TASK_NAME,
                    SET_RELEASE_VERSION_TASK_NAME,
                    FINALIZE_RELEASE_VERSION_TASK_NAME,
                    RELEASE_BUILD_TASK_NAME,
                    SET_SNAPSHOT_VERSION_TASK_NAME,
                    FINALIZE_SNAPSHOT_VERSION_TASK_NAME,
                    UPDATE_SCM_TASK_NAME
                )
            }
        }
    }

    private fun TaskContainer.registerEazyReleaseBaseTask(taskName: String) =
        register(taskName, EazyReleaseBaseTask::class.java)

    private fun TaskContainer.registerFinalizeReleaseVersionTask(projectDescriptorFactory: ProjectDescriptorFactory) =
        register(
            FINALIZE_RELEASE_VERSION_TASK_NAME,
            FinalizeReleaseVersionTask::class.java,
            projectDescriptorFactory,
            FinalizeReleaseVersionActionFactory()
        )

    private fun TaskContainer.registerFinalizeSnapshotVersionTask(projectDescriptorFactory: ProjectDescriptorFactory) =
        register(
            FINALIZE_SNAPSHOT_VERSION_TASK_NAME,
            FinalizeSnapshotVersionTask::class.java,
            projectDescriptorFactory,
            FinalizeSnapshotVersionActionFactory()
        )

    private fun TaskContainer.registerPrepareRepositoryForReleaseTask() =
        register(
            PREPARE_REPOSITORY_FOR_RELEASE_TASK_NAME,
            PrepareRepositoryForReleaseTask::class.java,
            PrepareRepositoryForReleaseActionFactory()
        )

    private fun TaskContainer.registerSetReleaseVersionTask(projectDescriptorFactory: ProjectDescriptorFactory) =
        register(
            SET_RELEASE_VERSION_TASK_NAME,
            SetReleaseVersionTask::class.java,
            projectDescriptorFactory,
            SetReleaseVersionActionFactory()
        )

    private fun TaskContainer.registerSetSnapshotVersionTask(projectDescriptorFactory: ProjectDescriptorFactory) =
        register(
            SET_SNAPSHOT_VERSION_TASK_NAME,
            SetSnapshotVersionTask::class.java,
            projectDescriptorFactory,
            SetSnapshotVersionActionFactory(),
        )

    private fun TaskContainer.registerUpdateScmTask(projectDescriptorFactory: ProjectDescriptorFactory) =
        register(UPDATE_SCM_TASK_NAME, UpdateScmTask::class.java, projectDescriptorFactory, UpdateScmActionFactory())

}
