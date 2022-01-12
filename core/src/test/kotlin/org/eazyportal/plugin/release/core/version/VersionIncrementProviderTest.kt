package org.eazyportal.plugin.release.core.version

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType
import org.eazyportal.plugin.release.core.version.model.VersionIncrement
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

internal class VersionIncrementProviderTest {

    private companion object {
        @JvmStatic
        fun provide() = listOf(
            Arguments.of(listOf("feature: message", "test: message", "invalid: message"), VersionIncrement.MINOR),
            Arguments.of(listOf("fix: message", "test: message", "invalid: message"), VersionIncrement.PATCH),
            Arguments.of(listOf("invalid: message", "test: message", "feature: message"), VersionIncrement.MINOR),
            Arguments.of(listOf("invalid: message", "test: message", "fix: message"), VersionIncrement.PATCH),

            Arguments.of(listOf("feature!: message", "test: message", "invalid: message"), VersionIncrement.MAJOR),
            Arguments.of(listOf("fix!: message", "test: message", "invalid: message"), VersionIncrement.MAJOR),
            Arguments.of(listOf("invalid: message", "test: message", "feature!: message"), VersionIncrement.MAJOR),
            Arguments.of(listOf("invalid: message", "test: message", "fix!: message"), VersionIncrement.MAJOR),

            Arguments.of(listOf("test: message", "invalid: message"), VersionIncrement.NONE)
        )

        @JvmStatic
        fun provideShouldFailWhen() = listOf(
            Arguments.of(listOf<String>(), ConventionalCommitType.DEFAULT_TYPES),
            Arguments.of(listOf("custom: message"), ConventionalCommitType.DEFAULT_TYPES),
            Arguments.of(listOf("invalid commit message"), ConventionalCommitType.DEFAULT_TYPES),

            Arguments.of(listOf("feature: message"), listOf<ConventionalCommitType>()),
            Arguments.of(listOf("feature: message"), listOf(ConventionalCommitType(listOf("test"), VersionIncrement.NONE))),
        )
    }

    @InjectMocks
    private lateinit var underTest: VersionIncrementProvider

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @MethodSource("provide")
    @ParameterizedTest
    fun test_provide(commits: List<String>, expected: VersionIncrement) {
        // GIVEN
        // WHEN
        // THEN
        val actual = underTest.provide(commits)

        assertThat(actual).isEqualTo(expected)
    }

    @MethodSource("provideShouldFailWhen")
    @ParameterizedTest
    fun test_provide_shouldFail(commits: List<String>, conventionalCommitTypes: List<ConventionalCommitType>) {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { underTest.provide(commits, conventionalCommitTypes) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("There are no acceptable commits.")
    }

}
