package org.eazyportal.plugin.release.jenkins.action;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction;
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;
import org.eazyportal.plugin.release.jenkins.project.MultiProjectActionsFactory;

import java.io.Serializable;

@Extension
public class SetSnapshotVersionActionFactory extends InvisibleAction implements Serializable {

    @Inject
    private transient MultiProjectActionsFactory multiProjectActionsFactory;
    @Inject
    private transient ReleaseStepConfig releaseStepConfig;
    @Inject
    private transient SnapshotVersionProvider snapshotVersionProvider;

    public SetSnapshotVersionAction create() {
        return new SetSnapshotVersionAction(
            multiProjectActionsFactory,
            releaseStepConfig.getScmActions(),
            releaseStepConfig.getScmConfig(),
            snapshotVersionProvider
        );
    }

}
