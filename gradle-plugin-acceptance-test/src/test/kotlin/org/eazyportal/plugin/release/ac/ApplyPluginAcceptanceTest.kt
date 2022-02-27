package org.eazyportal.plugin.release.ac

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.gradle.EazyReleasePlugin
import org.eazyportal.plugin.release.gradle.project.GradleProjectActionsFactory
import org.eazyportal.plugin.release.gradle.tasks.EazyReleaseBaseTask
import org.eazyportal.plugin.release.gradle.tasks.SetReleaseVersionTask
import org.eazyportal.plugin.release.gradle.tasks.SetSnapshotVersionTask
import org.eazyportal.plugin.release.gradle.tasks.UpdateScmTask
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginApplicationException
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
        project.tasks.let {
            assertThat(it.getByName(EazyReleasePlugin.SET_RELEASE_VERSION_TASK_NAME))
                .isInstanceOf(SetReleaseVersionTask::class.java)

            assertThat(it.getByName(EazyReleasePlugin.SET_SNAPSHOT_VERSION_TASK_NAME))
                .isInstanceOf(SetSnapshotVersionTask::class.java)

            assertThat(it.getByName(EazyReleasePlugin.UPDATE_SCM_TASK_NAME))
                .isInstanceOf(UpdateScmTask::class.java)

            assertThat(it.getByName(EazyReleasePlugin.RELEASE_TASK_NAME))
                .isInstanceOf(EazyReleaseBaseTask::class.java)
        }

        project.extensions
            .getByType(ExtraPropertiesExtension::class.java)
            .get(EazyReleasePlugin.PROJECT_ACTIONS_FACTORY_EXTRA_PROPERTY)
            .let { assertThat(it).isInstanceOf(GradleProjectActionsFactory::class.java) }
    }

    @Test
    fun test_withCustomProjectActions() {
        // GIVEN
        val projectActionsFactory = mock<ProjectActionsFactory>()

        // WHEN
        project.extensions.getByType(ExtraPropertiesExtension::class.java).apply {
            set(EazyReleasePlugin.PROJECT_ACTIONS_FACTORY_EXTRA_PROPERTY, projectActionsFactory)
        }

        // THEN
        project.plugins.apply {
            apply("org.eazyportal.plugin.release-gradle-plugin")
        }

        project.extensions
            .getByType(ExtraPropertiesExtension::class.java)
            .get(EazyReleasePlugin.PROJECT_ACTIONS_FACTORY_EXTRA_PROPERTY)
            .let { assertThat(it).isEqualTo(projectActionsFactory) }
    }

    @Test
    fun test_withInvalidProjectActions() {
        // GIVEN
        // WHEN
        project.extensions.getByType(ExtraPropertiesExtension::class.java).apply {
            set(EazyReleasePlugin.PROJECT_ACTIONS_FACTORY_EXTRA_PROPERTY, "invalid")
        }

        // THEN
        assertThatThrownBy {
            project.plugins.apply {
                apply("org.eazyportal.plugin.release-gradle-plugin")
            }
        }
        .isInstanceOf(PluginApplicationException::class.java)
        .cause
        .hasMessageContaining("class java.lang.String cannot be cast to class ${ProjectActions::class.java.name}")
    }

}
