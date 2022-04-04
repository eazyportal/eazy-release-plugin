package org.eazyportal.plugin.release.gradle

import org.eazyportal.plugin.release.gradle.action.SetReleaseVersionActionFactory
import org.eazyportal.plugin.release.gradle.action.SetSnapshotVersionActionFactory
import org.eazyportal.plugin.release.gradle.action.UpdateScmActionFactory
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension
import org.eazyportal.plugin.release.gradle.tasks.EazyReleaseBaseTask
import org.eazyportal.plugin.release.gradle.tasks.SetReleaseVersionTask
import org.eazyportal.plugin.release.gradle.tasks.SetSnapshotVersionTask
import org.eazyportal.plugin.release.gradle.tasks.UpdateScmTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class EazyReleasePlugin : Plugin<Project> {

    companion object {
        const val RELEASE_BUILD_TASK_NAME = "releaseBuild"
        const val RELEASE_TASK_NAME = "release"
        const val SET_RELEASE_VERSION_TASK_NAME = "setReleaseVersion"
        const val SET_SNAPSHOT_VERSION_TASK_NAME = "setSnapshotVersion"
        const val UPDATE_SCM_TASK_NAME = "updateScm"

        const val EXTENSION_NAME = "eazyRelease"
    }

    override fun apply(project: Project) {
        project.extensions.create(EXTENSION_NAME, EazyReleasePluginExtension::class.java)

        project.tasks.apply {
            register(SET_RELEASE_VERSION_TASK_NAME, SetReleaseVersionTask::class.java, SetReleaseVersionActionFactory())

            register(RELEASE_BUILD_TASK_NAME, EazyReleaseBaseTask::class.java).configure { task ->
                task.mustRunAfter(SET_RELEASE_VERSION_TASK_NAME)

                task.extension.releaseBuildTasks
                    .flatMap { releaseBuildTask ->
                        project.allprojects.mapNotNull {
                            it.tasks.findByName(releaseBuildTask)?.path
                        }
                    }
                    .run { task.dependsOn(this) }
            }

            register(SET_SNAPSHOT_VERSION_TASK_NAME, SetSnapshotVersionTask::class.java, SetSnapshotVersionActionFactory()).configure {
                it.mustRunAfter(RELEASE_BUILD_TASK_NAME)
            }

            register(UPDATE_SCM_TASK_NAME, UpdateScmTask::class.java, UpdateScmActionFactory()).configure {
                it.mustRunAfter(SET_SNAPSHOT_VERSION_TASK_NAME)
            }

            register(RELEASE_TASK_NAME, EazyReleaseBaseTask::class.java) {
                it.dependsOn(SET_RELEASE_VERSION_TASK_NAME, RELEASE_BUILD_TASK_NAME, SET_SNAPSHOT_VERSION_TASK_NAME, UPDATE_SCM_TASK_NAME)
            }
        }
    }

}
