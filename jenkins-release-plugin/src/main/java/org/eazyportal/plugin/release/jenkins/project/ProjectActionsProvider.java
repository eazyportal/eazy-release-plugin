package org.eazyportal.plugin.release.jenkins.project;

import org.eazyportal.plugin.release.core.project.ProjectActions;
import org.eazyportal.plugin.release.gradle.project.GradleProjectActions;
import org.eazyportal.plugin.release.jenkins.project.exception.InvalidProjectTypeException;

import java.io.File;
import java.nio.file.Path;

public final class ProjectActionsProvider {

    private ProjectActionsProvider() {
    }

    public static ProjectActions provide(File projectDir) {
        if (isGradleProject(projectDir.toPath())) {
            return new GradleProjectActions(projectDir);
        }

        throw new InvalidProjectTypeException("Unable to identify the project type.");
    }

    private static boolean isGradleProject(Path projectDirPath) {
        return projectDirPath.resolve(GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME).toFile().exists() ||
            projectDirPath.resolve("build.gradle").toFile().exists() ||
            projectDirPath.resolve("build.gradle.kts").toFile().exists();
    }

}
