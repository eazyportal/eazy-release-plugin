package org.eazyportal.plugin.release.gradle.action

import org.assertj.core.api.Assertions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class FinalizeSnapshotVersionActionFactoryTest {

    private lateinit var underTest: FinalizeSnapshotVersionActionFactory

    @BeforeEach
    fun setUp() {
        underTest = FinalizeSnapshotVersionActionFactory()
    }

    @Test
    fun test_create() {
        // GIVEN
        val extension: EazyReleasePluginExtension = mock()

        val projectActionsFactory: ProjectActionsFactory = mock()
        val scmActions: ScmActions = mock()

        // WHEN
        whenever(extension.projectActionsFactory).thenReturn(projectActionsFactory)
        whenever(extension.scmActions).thenReturn(scmActions)

        // THEN
        val actual = underTest.create(extension)

        Assertions.assertThat(actual).hasNoNullFieldsOrProperties()
    }

}
