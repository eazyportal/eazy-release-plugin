package org.eazyportal.plugin.release.core.version

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.core.version.model.Version
import org.eazyportal.plugin.release.core.version.model.VersionIncrement
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class ReleaseVersionProviderTest {

    companion object {
        private val SNAPSHOT_001 = Version(0, 0, 1, Version.DEVELOPMENT_VERSION_SUFFIX)
        private val RELEASE_001 = Version(0, 0, 1)
        private val RELEASE_002 = Version(0, 0, 2)
        private val RELEASE_010 = Version(0, 1, 0)
        private val RELEASE_100 = Version(1, 0, 0)

        @JvmStatic
        fun provide(): List<Arguments> {
            return listOf(
                Arguments.of(SNAPSHOT_001, VersionIncrement.PATCH, RELEASE_001),
                Arguments.of(SNAPSHOT_001, VersionIncrement.MINOR, RELEASE_010),
                Arguments.of(SNAPSHOT_001, VersionIncrement.MAJOR, RELEASE_100),

                Arguments.of(RELEASE_001, VersionIncrement.PATCH, RELEASE_002),
                Arguments.of(RELEASE_001, VersionIncrement.MINOR, RELEASE_010),
                Arguments.of(RELEASE_001, VersionIncrement.MAJOR, RELEASE_100),

                Arguments.of(Version(0, 0, 1, null, "invalid.version"), VersionIncrement.PATCH, RELEASE_001),

                Arguments.of(Version(0, 0, 1, "SNAPSHOT", null), VersionIncrement.PATCH, RELEASE_001)
            )
        }
    }

    private val underTest = ReleaseVersionProvider()

    @MethodSource("provide")
    @ParameterizedTest
    fun test_provide(version: Version, versionIncrement: VersionIncrement, expected: Version) {
        // GIVEN
        // WHEN
        // THEN
        val actual = underTest.provide(version, versionIncrement)

        assertThat(actual).isEqualTo(expected)
    }

}
