package org.eazyportal.plugin.release.jenkins;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import java.util.logging.Logger;

@Extension
public class ReleasePluginRunListener extends RunListener<Run<?, ?>> {

    private static final Logger LOGGER = Logger.getLogger(ReleasePluginRunListener.class.getName());

    private final ReleaseStepConfigAction releaseStepConfigAction = new ReleaseStepConfigAction();

    public ReleasePluginRunListener() {
        LOGGER.info("Initialize Release plugin.");
    }

    @Override
    public void onStarted(Run<?, ?> run, TaskListener listener) {
        run.addAction(releaseStepConfigAction);
    }

}
