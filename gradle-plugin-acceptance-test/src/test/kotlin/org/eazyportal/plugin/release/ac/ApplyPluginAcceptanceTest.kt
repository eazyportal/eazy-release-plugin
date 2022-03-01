package org.eazyportal.plugin.release.ac

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.tasks.EazyReleaseBaseTask
import org.eazyportal.plugin.release.gradle.tasks.SetReleaseVersionTask
import org.eazyportal.plugin.release.gradle.tasks.SetSnapshotVersionTask
import org.eazyportal.plugin.release.gradle.tasks.UpdateScmTask
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class ApplyPluginAcceptanceTest {

    @Test
    fun test() {
        // GIVEN
        val project = ProjectBuilder.builder()
            .build()

        // WHEN
        project.plugins.apply("org.eazyportal.plugin.release-gradle-plugin")

        // THEN
        project.tasks.run {
            withType(SetReleaseVersionTask::class.java) {
                assertThat(it.name).isEqualTo(EazyReleasePlugin.SET_RELEASE_VERSION_TASK_NAME)
            }

            withType(SetSnapshotVersionTask::class.java) {
                assertThat(it.name).isEqualTo(EazyReleasePlugin.SET_SNAPSHOT_VERSION_TASK_NAME)
            }

            withType(UpdateScmTask::class.java) {
                assertThat(it.name).isEqualTo(EazyReleasePlugin.UPDATE_SCM_TASK_NAME)
            }

            assertThat(withType(EazyReleaseBaseTask::class.java).map { it.name })
                .contains(EazyReleasePlugin.RELEASE_TASK_NAME, EazyReleasePlugin.RELEASE_BUILD_TASK_NAME)
        }
    }

}
