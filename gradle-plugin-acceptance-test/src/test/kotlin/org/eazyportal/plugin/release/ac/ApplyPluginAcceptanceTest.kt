package org.eazyportal.plugin.release.ac

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.tasks.EazyReleaseBaseTask
import org.eazyportal.plugin.release.gradle.tasks.SetReleaseVersionTask
import org.eazyportal.plugin.release.gradle.tasks.SetSnapshotVersionTask
import org.eazyportal.plugin.release.gradle.tasks.UpdateScmTask
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

class ApplyPluginAcceptanceTest {

    private lateinit var project: Project

    @BeforeEach
    fun setUp() {
        project = ProjectBuilder.builder()
            .build()
    }

    @Test
    fun test() {
        // GIVEN
        project.plugins.apply {
            apply("org.eazyportal.plugin.release-gradle-plugin")
        }

        // WHEN
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

    @Test
    fun test_withCustomProjectActions() {
        // GIVEN
        project.plugins.apply {
            apply("org.eazyportal.plugin.release-gradle-plugin")
        }

        val projectActionsFactory = mock<ProjectActionsFactory>()

        // WHEN
        project.extensions.getByType(ExtraPropertiesExtension::class.java).apply {
            set(EazyReleasePlugin.PROJECT_ACTIONS_FACTORY_EXTRA_PROPERTY, projectActionsFactory)
        }

        // THEN
        project.extensions
            .getByType(ExtraPropertiesExtension::class.java)
            .get(EazyReleasePlugin.PROJECT_ACTIONS_FACTORY_EXTRA_PROPERTY)
            .let { assertThat(it).isEqualTo(projectActionsFactory) }
    }

    @Test
    fun test_withInvalidProjectActions() {
        // GIVEN
        project.plugins.apply {
            apply("org.eazyportal.plugin.release-gradle-plugin")
        }

        // WHEN
        project.extensions.getByType(ExtraPropertiesExtension::class.java).apply {
            set(EazyReleasePlugin.PROJECT_ACTIONS_FACTORY_EXTRA_PROPERTY, "invalid")
        }
        // THEN
        assertThatThrownBy {
            project.tasks.withType(EazyReleaseBaseTask::class.java) {
                it.projectActionsFactory
            }
        }
        .isInstanceOf(ClassCastException::class.java)
        .hasMessageContaining("class java.lang.String cannot be cast to class ${ProjectActions::class.java.name}")
    }

}
