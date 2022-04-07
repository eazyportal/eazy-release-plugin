package org.eazyportal.plugin.release.jenkins.project;

import hudson.Extension;
import org.eazyportal.plugin.release.core.project.ProjectActions;
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory;
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectTypeException;
import org.eazyportal.plugin.release.core.project.exception.ProjectException;
import org.eazyportal.plugin.release.gradle.project.GradleProjectActions;

import java.io.File;

import static org.eazyportal.plugin.release.gradle.project.GradleProjectActionsFactoryKt.isGradleProjectDir;

@Extension
public class MultiProjectActionsFactory implements ProjectActionsFactory {

    @Override
    public ProjectActions create(File workingDir) throws ProjectException {
        if (isGradleProjectDir(workingDir)) {
            return new GradleProjectActions(workingDir);
        }
        else if (isNodeProject(workingDir)) {
            return new NodeProjectActions(workingDir);
        }

        throw new InvalidProjectTypeException("Unable to identify the project type.");
    }

    private static boolean isNodeProject(File workingDir) {
        return workingDir.toPath()
            .resolve(NodeProjectActions.PACKAGE_JSON_FILE_NAME)
            .toFile()
            .exists();
    }

}
