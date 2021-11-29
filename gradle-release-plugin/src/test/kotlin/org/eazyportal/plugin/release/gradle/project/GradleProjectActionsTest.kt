package org.eazyportal.plugin.release.gradle.project

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectLocationException
import org.eazyportal.plugin.release.core.project.exception.MissingProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.exception.MultipleProjectVersionPropertyException
import org.eazyportal.plugin.release.core.version.model.Version
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files

internal class GradleProjectActionsTest {

    companion object {
        @JvmStatic
        fun getVersion() = listOf(
            Arguments.of("version=0.1.0-SNAPSHOT", "0.1.0-SNAPSHOT"),
            Arguments.of("version= 0.1.0-SNAPSHOT", "0.1.0-SNAPSHOT"),
            Arguments.of("version =0.1.0-SNAPSHOT", "0.1.0-SNAPSHOT"),
            Arguments.of("version = 0.1.0-SNAPSHOT", "0.1.0-SNAPSHOT"),
            Arguments.of("  version = 0.1.0-SNAPSHOT  ", "0.1.0-SNAPSHOT"),
            Arguments.of("version = 0.1.0-alpha", "0.1.0-alpha"),
            Arguments.of("version = 0.1.0-alpha+b123", "0.1.0-alpha+b123")
        )

        @JvmStatic
        fun setVersion() = listOf(
            Arguments.of("", Version(0, 1, 0, "SNAPSHOT")),
            Arguments.of("version = 0.0.1", Version(0, 1, 0, "SNAPSHOT")),
            Arguments.of(
                """
                property1 = value1

                property2 = value2
                """.trimIndent(),
                Version(0, 1, 0, "SNAPSHOT"))
        )
    }

    private val workingDir = Files.createTempDirectory("").toFile()
    private val gradlePropertiesFile = workingDir.resolve(GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME)

    private val underTest = GradleProjectActions(workingDir)

    @AfterEach
    fun tearDown() {
        gradlePropertiesFile.delete()
    }

    @Test
    fun test_constructor_shouldFail_whenWorkingDirIsFile() {
        // GIVEN
        workingDir.deleteRecursively()
        workingDir.writeText("")

        // WHEN
        // THEN
        assertThatThrownBy { GradleProjectActions(workingDir) }
            .isInstanceOf(InvalidProjectLocationException::class.java)
            .hasMessage("Invalid Gradle project location: $workingDir")
    }

    @Test
    fun test_getVersion_shouldFail_whenWorkingDirIsMissing() {
        // GIVEN
        workingDir.deleteRecursively()

        // WHEN
        // THEN
        assertThatThrownBy { GradleProjectActions(workingDir) }
            .isInstanceOf(InvalidProjectLocationException::class.java)
            .hasMessage("Invalid Gradle project location: $workingDir")
    }

    @MethodSource("getVersion")
    @ParameterizedTest
    fun test_getVersion(property: String, expected: String) {
        // GIVEN
        gradlePropertiesFile.writeText(property)

        // WHEN
        // THEN
        val actual = underTest.getVersion()

        assertThat(actual.toString())
            .isEqualTo(expected)
    }

    @Test
    fun test_getVersion_shouldFail_whenGradlePropertiesFileIsMissing() {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy { underTest.getVersion() }
            .isInstanceOf(InvalidProjectLocationException::class.java)
            .hasMessage("'gradle.properties' file is missing: $gradlePropertiesFile")
    }

    @Test
    fun test_getVersion_shouldFail_whenVersionPropertyIsMissing() {
        // GIVEN
        gradlePropertiesFile.writeText("property1 = value1")

        // WHEN
        // THEN
        assertThatThrownBy { underTest.getVersion() }
            .isInstanceOf(MissingProjectVersionPropertyException::class.java)
            .hasMessage("The project does not have version property.")
    }

    @Test
    fun test_getVersion_shouldFail_whenMultipleVersionPropertyPresent() {
        // GIVEN
        gradlePropertiesFile.writeText(
            """
            version = 0.0.0
            version = 0.0.1
        """.trimIndent()
        )

        // WHEN
        // THEN
        assertThatThrownBy { underTest.getVersion() }
            .isInstanceOf(MultipleProjectVersionPropertyException::class.java)
            .hasMessage("The project has multiple versions: [0.0.0, 0.0.1]")
    }

    @Test
    fun test_scmFilesToCommit() {
        // GIVEN
        // WHEN
        // THEN
        val actual = underTest.scmFilesToCommit()

        assertThat(actual).isNotEmpty
    }

    @MethodSource("setVersion")
    @ParameterizedTest
    fun test_setVersion(initialProperties: String, version: Version) {
        // GIVEN
        gradlePropertiesFile.writeText(initialProperties)

        // WHEN
        // THEN
        underTest.setVersion(version)

        assertThat(gradlePropertiesFile.readText())
            .contains("version = $version")
    }

    @Test
    fun test_setVersion_shouldFail_whenMultipleVersionPropertyPresent() {
        // GIVEN
        gradlePropertiesFile.writeText("""
            version = 0.0.0
            version = 0.0.1
        """.trimIndent())

        // WHEN
        // THEN
        assertThatThrownBy { underTest.setVersion(Version(0, 0, 1)) }
            .isInstanceOf(MultipleProjectVersionPropertyException::class.java)
            .hasMessage("The project has multiple versions: [version = 0.0.0, version = 0.0.1]")
    }

}
