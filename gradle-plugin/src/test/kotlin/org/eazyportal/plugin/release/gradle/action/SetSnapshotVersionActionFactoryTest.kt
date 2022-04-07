package org.eazyportal.plugin.release.gradle.action

import org.assertj.core.api.Assertions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class SetSnapshotVersionActionFactoryTest {

    private lateinit var underTest: SetSnapshotVersionActionFactory

    @BeforeEach
    fun setUp() {
        underTest = SetSnapshotVersionActionFactory()
    }

    @Test
    fun test_create() {
        val extension = mock<EazyReleasePluginExtension>()

        val projectActionsFactory = mock<ProjectActionsFactory>()
        val scmActions = mock<ScmActions>()
        val scmConfig = mock<ScmConfig>()

        // WHEN
        whenever(extension.projectActionsFactory).thenReturn(projectActionsFactory)
        whenever(extension.scmActions).thenReturn(scmActions)
        whenever(extension.scmConfig).thenReturn(scmConfig)

        // THEN
        val actual = underTest.create(extension)

        Assertions.assertThat(actual.scmActions).isEqualTo(scmActions)
        Assertions.assertThat(actual.scmConfig).isEqualTo(scmConfig)
        Assertions.assertThat(actual).hasNoNullFieldsOrProperties()
    }

}