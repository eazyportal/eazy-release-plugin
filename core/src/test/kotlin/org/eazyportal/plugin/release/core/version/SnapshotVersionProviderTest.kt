package org.eazyportal.plugin.release.core.version

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.version.exception.InvalidVersionException
import org.eazyportal.plugin.release.core.version.model.Version
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class SnapshotVersionProviderTest {

    companion object {
        @JvmStatic
        private val RELEASE_001 = Version(0, 0, 1)
        @JvmStatic
        private val SNAPSHOT_002 = Version(0, 0, 2, Version.DEVELOPMENT_VERSION_SUFFIX)
    }

    private val underTest = SnapshotVersionProvider()

    @Test
    fun test_provide() {
        // GIVEN
        // WHEN
        // THEN
        val actual = underTest.provide(RELEASE_001)

        assertThat(actual).isEqualTo(SNAPSHOT_002)
    }

    @Test
    fun test_provide_shouldFail_whenSnapshotVersionProvided() {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { underTest.provide(SNAPSHOT_002) }
            .isInstanceOf(InvalidVersionException::class.java)
            .hasMessage("Failed to set SNAPSHOT version, because project already on SNAPSHOT version.")
    }

}
