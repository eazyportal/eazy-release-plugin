package org.eazyportal.plugin.release.jenkins;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.listeners.RunListener;
import org.eazyportal.plugin.release.jenkins.action.ActionContextFactory;
import org.eazyportal.plugin.release.jenkins.action.ReleaseActionFactory;
import org.eazyportal.plugin.release.jenkins.project.ProjectDescriptorFactory;
import org.eazyportal.plugin.release.jenkins.scm.ScmActionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Extension
public class ReleasePluginRunListener extends RunListener<Run<?, ?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReleasePluginRunListener.class);

    @Inject
    private ActionContextFactory actionContextFactory;
    @Inject
    private ProjectDescriptorFactory projectDescriptorFactory;
    @Inject
    private ReleaseActionFactory releaseActionFactory;
    @Inject
    private ScmActionFactory scmActionFactory;

    public ReleasePluginRunListener() {
        LOGGER.info("Initialize EazyRelease plugin.");
    }

    @Override
    public void onInitialize(Run<?, ?> run) {
        run.addAction(actionContextFactory);
        run.addAction(projectDescriptorFactory);
        run.addAction(releaseActionFactory);
        run.addAction(scmActionFactory);
    }

}
