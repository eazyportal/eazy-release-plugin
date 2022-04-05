package org.eazyportal.plugin.release.gradle.action

import org.assertj.core.api.Assertions
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class PrepareRepositoryForReleaseActionFactoryTest {

    private lateinit var underTest: PrepareRepositoryForReleaseActionFactory

    @BeforeEach
    fun setUp() {
        underTest = PrepareRepositoryForReleaseActionFactory()
    }

    @Test
    fun test_create() {
        // GIVEN
        val extension: EazyReleasePluginExtension = mock()

        val scmActions: ScmActions = mock()
        val scmConfig: ScmConfig = mock()

        // WHEN
        whenever(extension.scmActions).thenReturn(scmActions)
        whenever(extension.scmConfig).thenReturn(scmConfig)

        // THEN
        val actual = underTest.create(extension)

        Assertions.assertThat(actual).hasNoNullFieldsOrProperties()
    }

}
