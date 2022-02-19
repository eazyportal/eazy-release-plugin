package org.eazyportal.plugin.release.core.version

import org.assertj.core.api.Assertions.assertThat
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
            Arguments.of(listOf("feature: message", "test: message", "invalid: message"), ConventionalCommitType.DEFAULT_TYPES, VersionIncrement.MINOR),
            Arguments.of(listOf("fix: message", "test: message", "invalid: message"), ConventionalCommitType.DEFAULT_TYPES, VersionIncrement.PATCH),
            Arguments.of(listOf("invalid: message", "test: message", "feature: message"), ConventionalCommitType.DEFAULT_TYPES, VersionIncrement.MINOR),
            Arguments.of(listOf("invalid: message", "test: message", "fix: message"), ConventionalCommitType.DEFAULT_TYPES, VersionIncrement.PATCH),

            Arguments.of(listOf("feature!: message", "test: message", "invalid: message"), ConventionalCommitType.DEFAULT_TYPES, VersionIncrement.MAJOR),
            Arguments.of(listOf("fix!: message", "test: message", "invalid: message"), ConventionalCommitType.DEFAULT_TYPES, VersionIncrement.MAJOR),
            Arguments.of(listOf("invalid: message", "test: message", "feature!: message"), ConventionalCommitType.DEFAULT_TYPES, VersionIncrement.MAJOR),
            Arguments.of(listOf("invalid: message", "test: message", "fix!: message"), ConventionalCommitType.DEFAULT_TYPES, VersionIncrement.MAJOR),

            Arguments.of(listOf("test: message", "invalid: message"), ConventionalCommitType.DEFAULT_TYPES, VersionIncrement.NONE),

            Arguments.of(listOf<String>(), ConventionalCommitType.DEFAULT_TYPES, null),
            Arguments.of(listOf("custom: message"), ConventionalCommitType.DEFAULT_TYPES, null),
            Arguments.of(listOf("invalid commit message"), ConventionalCommitType.DEFAULT_TYPES, null),

            Arguments.of(listOf("feature: message"), listOf<ConventionalCommitType>(), null),
            Arguments.of(listOf("feature: message"), listOf(ConventionalCommitType(listOf("test"), VersionIncrement.NONE)), null),
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
    fun test_provide(commits: List<String>, conventionalCommitTypes: List<ConventionalCommitType>, expected: VersionIncrement?) {
        // GIVEN
        // WHEN
        // THEN
        val actual = underTest.provide(commits, conventionalCommitTypes)

        assertThat(actual).isEqualTo(expected)
    }

}
