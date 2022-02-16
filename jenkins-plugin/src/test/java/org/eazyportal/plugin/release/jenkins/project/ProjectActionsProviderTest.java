package org.eazyportal.plugin.release.jenkins.project;

import com.google.common.io.Files;
import org.eazyportal.plugin.release.core.project.ProjectActions;
import org.eazyportal.plugin.release.gradle.project.GradleProjectActions;
import org.eazyportal.plugin.release.jenkins.project.exception.InvalidProjectTypeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectActionsProviderTest {

    private File workingDir;

    @BeforeEach
    void setUp() {
        workingDir = Files.createTempDir();
    }

    @AfterEach
    void testDown() {
        workingDir.deleteOnExit();
    }

    @CsvSource({ GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME, "build.gradle", "build.gradle.kts" })
    @ParameterizedTest
    void test_provide_GradleProject(String fileName) throws IOException {
        // GIVEN
        new File(workingDir, fileName).createNewFile();

        // WHEN
        // THEN
        ProjectActions actual = ProjectActionsProvider.provide(workingDir);

        assertThat(actual).isInstanceOf(GradleProjectActions.class);
    }

    @Test
    void test_provide_InvalidProject() {
        // GIVEN
        // WHEN
        // THEN
        assertThatThrownBy(() -> ProjectActionsProvider.provide(workingDir))
            .isInstanceOf(InvalidProjectTypeException.class)
            .hasMessage("Unable to identify the project type.");
    }

}
