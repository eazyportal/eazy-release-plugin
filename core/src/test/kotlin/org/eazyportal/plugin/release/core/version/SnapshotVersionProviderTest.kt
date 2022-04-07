package org.eazyportal.plugin.release.core.version

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.version.exception.InvalidVersionException
import org.eazyportal.plugin.release.core.version.model.VersionFixtures
import org.junit.jupiter.api.Test

internal class SnapshotVersionProviderTest {

    private val underTest = SnapshotVersionProvider()

    @Test
    fun test_provide() {
        // GIVEN
        // WHEN
        // THEN
        val actual = underTest.provide(VersionFixtures.RELEASE_001)

        assertThat(actual).isEqualTo(VersionFixtures.SNAPSHOT_002)
    }

    @Test
    fun test_provide_shouldFail_whenSnapshotVersionProvided() {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { underTest.provide(VersionFixtures.SNAPSHOT_002) }
            .isInstanceOf(InvalidVersionException::class.java)
            .hasMessage("Failed to set SNAPSHOT version, because project already on SNAPSHOT version.")
    }

}
