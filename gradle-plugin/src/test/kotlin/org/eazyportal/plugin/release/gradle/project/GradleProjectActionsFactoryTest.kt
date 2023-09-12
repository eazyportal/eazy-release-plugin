package org.eazyportal.plugin.release.gradle.project

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectTypeException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.File

internal class GradleProjectActionsFactoryTest {

    @TempDir
    private lateinit var workingDir: File

    private lateinit var underTest: GradleProjectActionsFactory

    @BeforeEach
    fun setUp() {
        underTest = GradleProjectActionsFactory()
    }

    @CsvSource(value = ["build.gradle", "build.gradle.kts", "settings.gradle", "settings.gradle.kts"])
    @ParameterizedTest
    fun test_provide_GradleProject(fileName: String) {
        // GIVEN
        File(workingDir, fileName).createNewFile()

        // WHEN
        // THEN
        val actual: ProjectActions = underTest.create(workingDir)

        assertThat(actual).isInstanceOf(GradleProjectActions::class.java)
    }

    @Test
    fun test_provide_InvalidProject() {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { underTest.create(workingDir) }
            .isInstanceOf(InvalidProjectTypeException::class.java)
            .hasMessageStartingWith("Unable to identify the project type in: ")
    }

}
