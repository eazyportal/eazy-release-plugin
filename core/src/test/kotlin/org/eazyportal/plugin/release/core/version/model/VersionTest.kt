package org.eazyportal.plugin.release.core.version.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class VersionTest {

    companion object {
        @JvmStatic
        fun init_invalidVersions() = listOf(
            Arguments.of(0, 0, -1),
            Arguments.of(0, -1, 0),
            Arguments.of(-1, 0, 0)
        )

        @JvmStatic
        fun init_validVersions() = listOf(
            Arguments.of(0, 0, 1, null, null),
            Arguments.of(0, 0, 1, "", ""),
            Arguments.of(0, 0, 1, "SNAPSHOT", null),
            Arguments.of(0, 0, 1, "SNAPSHOT", ""),
            Arguments.of(0, 0, 1, "SNAPSHOT", "b123"),
        )

        @JvmStatic
        fun of_validVersions() = listOf(
            Arguments.of("1.0.0", Version(1, 0, 0)),
            Arguments.of("1.0.0-SNAPSHOT", Version(1, 0, 0, "SNAPSHOT")),
            Arguments.of("1.0.0-SNAPSHOT+b123", Version(1, 0, 0, "SNAPSHOT", "b123")),
            Arguments.of("1.0.0+b123", Version(1, 0, 0, null, "b123"))
        )

        @JvmStatic
        fun of_invalidVersions() = listOf(
            Arguments.of("1", "Failed to parse provided version: 1"),
            Arguments.of("1.0", "Failed to parse provided version: 1.0"),
            Arguments.of("-1.0.0", "Failed to parse provided version: -1.0.0"),
            Arguments.of("1.0-SNAPSHOT", "Failed to parse provided version: 1.0-SNAPSHOT"),
            Arguments.of("1.0.0-0", "Pre-release should not start with '0'.")
        )
    }

    @Test
    fun test_compare() {
        // GIVEN
        val version1 = Version(1, 0, 0)
        val version2 = Version(2, 0, 0)
        val version3 = Version(3, 0, 0)

        // WHEN
        // THEN
        assertThat(version1 < version2).isTrue
        assertThat(version2 < version3).isTrue
        assertThat(version3 > version1).isTrue
        assertThat(version2 == version2).isTrue
    }

    @MethodSource("init_validVersions")
    @ParameterizedTest
    fun test_init(major: Int, minor: Int, patch: Int, preRelease: String?, build: String? = null) {
        // GIVEN
        // WHEN
        // THEN
        val actual = Version(major, minor, patch, preRelease, build)

        assertThat(actual).isNotNull
    }

    @MethodSource("init_invalidVersions")
    @ParameterizedTest
    fun test_init_shouldFail_whenVersionIsNegative(major: Int, minor: Int, patch: Int) {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { Version(major, minor, patch) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Version cannot have negative major, minor, or patch values.")
    }

    @Test
    fun test_init_shouldFail_whenPreReleaseStartWithZero() {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { Version(0, 0, 0, "0") }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Pre-release should not start with '0'.")
    }

    @MethodSource("of_validVersions")
    @ParameterizedTest
    fun test_of(versionValue: String, expected: Version) {
        // GIVEN
        // WHEN
        // THEN
        assertThat(Version.of(versionValue))
            .isEqualTo(expected)
    }

    @MethodSource("of_invalidVersions")
    @ParameterizedTest
    fun test_of_withInvalidVersions(versionValue: String, errorMessage: String) {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { Version.of(versionValue) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(errorMessage)
    }

}
