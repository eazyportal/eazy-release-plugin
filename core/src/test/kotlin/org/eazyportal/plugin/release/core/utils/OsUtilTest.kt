package org.eazyportal.plugin.release.core.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class OsUtilTest {

    companion object {
        @JvmStatic
        fun osNames(): List<Arguments> =
            listOf(
                Arguments.of("Linux", false),
                Arguments.of("Windows", true)
            )
    }

    @MethodSource("osNames")
    @ParameterizedTest
    fun test_isWindows(osName: String, expected: Boolean) {
        // GIVEN
        System.setProperty("os.name", osName)

        // WHEN
        // THEN
        assertThat(isWindows()).isEqualTo(expected)
    }

}
