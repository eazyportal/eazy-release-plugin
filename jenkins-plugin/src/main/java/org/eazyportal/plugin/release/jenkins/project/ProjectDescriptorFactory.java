package org.eazyportal.plugin.release.jenkins.project;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.project.model.ProjectDescriptor;
import org.eazyportal.plugin.release.core.project.model.ProjectFile;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.gradle.project.GradleProjectActionsFactory;

import javax.inject.Inject;
import java.io.Serializable;

@Extension
public class ProjectDescriptorFactory extends InvisibleAction implements Serializable {

    @Inject
    private transient GradleProjectActionsFactory projectActionsFactory;

    private final transient org.eazyportal.plugin.release.core.project.ProjectDescriptorFactory<FilePath> delegate =
        new org.eazyportal.plugin.release.core.project.ProjectDescriptorFactory<>();

    public ProjectDescriptor<FilePath> create(ProjectFile<FilePath> projectFile, ScmActions<FilePath> scmActions) {
        return delegate.create(projectActionsFactory, scmActions, projectFile);
    }

}
