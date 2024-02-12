package org.eazyportal.plugin.release.gradle.project

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectLocationException
import org.eazyportal.plugin.release.core.project.exception.MissingProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.exception.MultipleProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.exception.ProjectVersionPropertyException
import org.eazyportal.plugin.release.core.project.model.FileSystemProjectFile
import org.eazyportal.plugin.release.core.version.model.VersionFixtures
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
            Arguments.of("version=0.1.0-SNAPSHOT", VersionFixtures.SNAPSHOT_010.toString()),
            Arguments.of("version= 0.1.0-SNAPSHOT", VersionFixtures.SNAPSHOT_010.toString()),
            Arguments.of("version =0.1.0-SNAPSHOT", VersionFixtures.SNAPSHOT_010.toString()),
            Arguments.of("version = 0.1.0-SNAPSHOT", VersionFixtures.SNAPSHOT_010.toString()),
            Arguments.of("  version = 0.1.0-SNAPSHOT  ", VersionFixtures.SNAPSHOT_010.toString()),
            Arguments.of("version = 0.1.0-alpha", "0.1.0-alpha"),
            Arguments.of("version = 0.1.0-alpha+b123", "0.1.0-alpha+b123")
        )

        @JvmStatic
        fun setVersion_shouldFail_whenVersionPropertyIsMissing() = listOf(
            Arguments.of(""),
            Arguments.of(
                """
                property1 = value1

                property2 = value2
                """.trimIndent()
            )
        )
    }

    private val workingDir = Files.createTempDirectory("").toFile()
    private val gradlePropertiesFile = workingDir.resolve(GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME)

    private val underTest = GradleProjectActions(FileSystemProjectFile(workingDir))

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
        assertThatThrownBy { GradleProjectActions(FileSystemProjectFile(workingDir)) }
            .isInstanceOf(InvalidProjectLocationException::class.java)
            .hasMessage("Invalid Gradle project location: $workingDir")
    }

    @Test
    fun test_getVersion_shouldFail_whenWorkingDirIsMissing() {
        // GIVEN
        workingDir.deleteRecursively()

        // WHEN
        // THEN
        assertThatThrownBy { GradleProjectActions(FileSystemProjectFile(workingDir)) }
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
            .hasMessage("'${GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME}' file is missing in: $workingDir")
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

    @Test
    fun test_setVersion() {
        // GIVEN
        gradlePropertiesFile.writeText("version = 0.0.1")

        // WHEN
        // THEN
        underTest.setVersion(VersionFixtures.RELEASE_002)

        assertThat(gradlePropertiesFile.readText())
            .contains("version = ${VersionFixtures.RELEASE_002}")
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
        assertThatThrownBy { underTest.setVersion(VersionFixtures.RELEASE_001) }
            .isInstanceOf(MultipleProjectVersionPropertyException::class.java)
            .hasMessage("The project has multiple versions: [version = 0.0.0, version = 0.0.1]")
    }

    @MethodSource("setVersion_shouldFail_whenVersionPropertyIsMissing")
    @ParameterizedTest
    fun test_setVersion_shouldFail_whenVersionPropertyIsMissing(initialProperties: String) {
        // GIVEN
        gradlePropertiesFile.writeText(initialProperties)

        // WHEN
        // THEN
        assertThatThrownBy { underTest.setVersion(VersionFixtures.RELEASE_001) }
            .isInstanceOf(ProjectVersionPropertyException::class.java)
            .hasMessage("The project does not have version property.")
    }

}
