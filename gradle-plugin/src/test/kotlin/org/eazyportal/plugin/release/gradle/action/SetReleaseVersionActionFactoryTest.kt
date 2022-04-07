package org.eazyportal.plugin.release.gradle.action

import org.assertj.core.api.Assertions
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.eazyportal.plugin.release.core.scm.ScmActions
import org.eazyportal.plugin.release.core.scm.model.ScmConfig
import org.eazyportal.plugin.release.core.version.model.VersionIncrement.MAJOR
import org.eazyportal.plugin.release.gradle.model.EazyReleasePluginExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class SetReleaseVersionActionFactoryTest {

    private companion object {
        @JvmStatic
        fun create() = listOf(
            Arguments.of(listOf<ConventionalCommitType>()),
            Arguments.of(listOf(ConventionalCommitType(listOf("dummy"), MAJOR))),
            Arguments.of(ConventionalCommitType.DEFAULT_TYPES)
        )
    }

    private lateinit var underTest: SetReleaseVersionActionFactory

    @BeforeEach
    fun setUp() {
        underTest = SetReleaseVersionActionFactory()
    }

    @MethodSource("create")
    @ParameterizedTest
    fun test_create(conventionalCommitTypes: List<ConventionalCommitType>) {
        // GIVEN
        val extension = mock<EazyReleasePluginExtension>()

        val projectActionsFactory = mock<ProjectActionsFactory>()
        val scmActions = mock<ScmActions>()
        val scmConfig = mock<ScmConfig>()

        // WHEN
        whenever(extension.conventionalCommitTypes).thenReturn(conventionalCommitTypes)
        whenever(extension.projectActionsFactory).thenReturn(projectActionsFactory)
        whenever(extension.scmActions).thenReturn(scmActions)
        whenever(extension.scmConfig).thenReturn(scmConfig)

        // THEN
        val actual = underTest.create(extension)

        Assertions.assertThat(actual).hasNoNullFieldsOrProperties()
    }

}