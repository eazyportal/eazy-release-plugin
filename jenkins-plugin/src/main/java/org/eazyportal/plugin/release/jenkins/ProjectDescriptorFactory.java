package org.eazyportal.plugin.release.jenkins;

import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.model.ProjectDescriptor;
import org.eazyportal.plugin.release.core.project.ProjectActionsFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.Serializable;

@Extension
public class ProjectDescriptorFactory extends InvisibleAction implements Serializable {

    @Inject
    private transient ProjectActionsFactory projectActionsFactory;
    @Inject
    private transient ReleaseStepConfig releaseStepConfig;

    private final transient org.eazyportal.plugin.release.core.ProjectDescriptorFactory delegate =
        new org.eazyportal.plugin.release.core.ProjectDescriptorFactory();

    public ProjectDescriptor create(File workingDir) {
        return delegate.create(projectActionsFactory, releaseStepConfig.getScmActions(), workingDir);
    }

}
