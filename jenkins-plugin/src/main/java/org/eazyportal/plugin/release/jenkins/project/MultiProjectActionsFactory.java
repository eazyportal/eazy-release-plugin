package org.eazyportal.plugin.release.jenkins.project;

import hudson.Extension;
import org.eazyportal.plugin.release.core.project.ProjectActions;
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory;
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectTypeException;
import org.eazyportal.plugin.release.gradle.project.GradleProjectActions;

import java.io.File;

import static org.eazyportal.plugin.release.gradle.project.GradleProjectActionsFactoryKt.isGradleProject;

@Extension
public class MultiProjectActionsFactory implements ProjectActionsFactory {

    @Override
    public ProjectActions create(File workingDir) throws InvalidProjectTypeException {
        if (isGradleProject(workingDir.toPath())) {
            return new GradleProjectActions(workingDir);
        }

        throw new InvalidProjectTypeException("Unable to identify the project type.");
    }

}
