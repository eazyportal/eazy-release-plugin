package org.eazyportal.plugin.release.jenkins;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.listeners.RunListener;
import org.eazyportal.plugin.release.jenkins.action.SetReleaseVersionActionFactory;
import org.eazyportal.plugin.release.jenkins.action.SetSnapshotVersionActionFactory;
import org.eazyportal.plugin.release.jenkins.action.UpdateScmActionFactory;

import java.util.logging.Logger;

@Extension
public class ReleasePluginRunListener extends RunListener<Run<?, ?>> {

    private static final Logger LOGGER = Logger.getLogger(ReleasePluginRunListener.class.getName());

    @Inject
    private SetReleaseVersionActionFactory setReleaseVersionActionFactory;
    @Inject
    private SetSnapshotVersionActionFactory setSnapshotVersionActionFactory;
    @Inject
    private UpdateScmActionFactory updateScmActionFactory;

    public ReleasePluginRunListener() {
        LOGGER.info("Initialize EazyRelease plugin.");
    }

    @Override
    public void onInitialize(Run<?, ?> run) {
        run.addAction(setReleaseVersionActionFactory);
        run.addAction(setSnapshotVersionActionFactory);
        run.addAction(updateScmActionFactory);
    }

}
