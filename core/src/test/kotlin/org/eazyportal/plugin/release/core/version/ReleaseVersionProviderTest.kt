package org.eazyportal.plugin.release.core.version

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.core.version.model.Version
import org.eazyportal.plugin.release.core.version.model.VersionFixtures
import org.eazyportal.plugin.release.core.version.model.VersionIncrement
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class ReleaseVersionProviderTest {

    companion object {
        @JvmStatic
        fun provide(): List<Arguments> {
            return listOf(
                Arguments.of(VersionFixtures.SNAPSHOT_001, VersionIncrement.PATCH, VersionFixtures.RELEASE_001),
                Arguments.of(VersionFixtures.SNAPSHOT_001, VersionIncrement.MINOR, VersionFixtures.RELEASE_010),
                Arguments.of(VersionFixtures.SNAPSHOT_001, VersionIncrement.MAJOR, VersionFixtures.RELEASE_100),

                Arguments.of(VersionFixtures.SNAPSHOT_010, VersionIncrement.PATCH, VersionFixtures.RELEASE_010),
                Arguments.of(VersionFixtures.SNAPSHOT_010, VersionIncrement.MINOR, VersionFixtures.RELEASE_020),
                Arguments.of(VersionFixtures.SNAPSHOT_010, VersionIncrement.MAJOR, VersionFixtures.RELEASE_100),

                Arguments.of(VersionFixtures.SNAPSHOT_100, VersionIncrement.PATCH, VersionFixtures.RELEASE_100),
                Arguments.of(VersionFixtures.SNAPSHOT_100, VersionIncrement.MINOR, Version(1, 1, 0)),
                Arguments.of(VersionFixtures.SNAPSHOT_100, VersionIncrement.MAJOR, VersionFixtures.RELEASE_200),

                Arguments.of(VersionFixtures.RELEASE_001, VersionIncrement.PATCH, VersionFixtures.RELEASE_002),
                Arguments.of(VersionFixtures.RELEASE_001, VersionIncrement.MINOR, VersionFixtures.RELEASE_010),
                Arguments.of(VersionFixtures.RELEASE_001, VersionIncrement.MAJOR, VersionFixtures.RELEASE_100),

                Arguments.of(Version(0, 0, 1, null, "invalid.version"), VersionIncrement.PATCH, VersionFixtures.RELEASE_001),

                Arguments.of(Version(0, 0, 1, "SNAPSHOT", null), VersionIncrement.PATCH, VersionFixtures.RELEASE_001)
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
