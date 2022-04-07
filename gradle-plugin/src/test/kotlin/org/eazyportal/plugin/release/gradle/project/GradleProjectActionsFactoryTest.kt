package org.eazyportal.plugin.release.gradle.project

import org.assertj.core.api.Assertions
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

    @CsvSource(value = [GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME, "build.gradle", "build.gradle.kts"])
    @ParameterizedTest
    fun test_provide_GradleProject(fileName: String) {
        // GIVEN
        File(workingDir, fileName).createNewFile()

        // WHEN
        // THEN

        // WHEN
        // THEN
        val actual: ProjectActions = underTest.create(workingDir)

        Assertions.assertThat(actual).isInstanceOf(GradleProjectActions::class.java)
    }

    @Test
    fun test_provide_InvalidProject() {
        // GIVEN
        // WHEN
        // THEN
        Assertions.assertThatThrownBy { underTest.create(workingDir) }
            .isInstanceOf(InvalidProjectTypeException::class.java)
            .hasMessage("Unable to identify the project type.")
    }


}
