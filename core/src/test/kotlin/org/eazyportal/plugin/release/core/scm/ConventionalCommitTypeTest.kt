package org.eazyportal.plugin.release.core.scm

import org.assertj.core.api.Assertions
import org.eazyportal.plugin.release.core.version.model.VersionIncrement.PATCH
import org.junit.jupiter.api.Test

internal class ConventionalCommitTypeTest {

    @Test
    fun test_constructor_shouldFail_whenThereAreNoAliases() {
        // GIVEN
        // WHEN
        // THEN
        Assertions.assertThatThrownBy { ConventionalCommitType(listOf(), PATCH) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Required to have at least 1 alias for each type.")
    }

}