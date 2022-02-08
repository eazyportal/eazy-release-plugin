package org.eazyportal.plugin.release.ac

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.tasks.EazyReleaseBaseTask
import org.eazyportal.plugin.release.gradle.tasks.SetReleaseVersionTask
import org.eazyportal.plugin.release.gradle.tasks.SetSnapshotVersionTask
import org.eazyportal.plugin.release.gradle.tasks.UpdateScmTask
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class ApplyPluginTest {

    @Test
    fun test() {
        // GIVEN
        val project: Project = ProjectBuilder.builder()
            .build()
            .also {
                it.plugins.apply {
                    apply("java")
                    apply("org.eazyportal.plugin.release-gradle-plugin")
                }
            }

        // WHEN
        // THEN
        assertThat(project.tasks.getByName(EazyReleasePlugin.SET_RELEASE_VERSION_TASK_NAME))
            .isInstanceOf(SetReleaseVersionTask::class.java)

        assertThat(project.tasks.getByName(EazyReleasePlugin.SET_SNAPSHOT_VERSION_TASK_NAME))
            .isInstanceOf(SetSnapshotVersionTask::class.java)

        assertThat(project.tasks.getByName(EazyReleasePlugin.UPDATE_SCM_TASK_NAME))
            .isInstanceOf(UpdateScmTask::class.java)

        assertThat(project.tasks.getByName(EazyReleasePlugin.RELEASE_TASK_NAME))
            .isInstanceOf(EazyReleaseBaseTask::class.java)
    }

}
