package org.eazyportal.plugin.release.jenkins.action;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;
import org.eazyportal.plugin.release.jenkins.project.MultiProjectActionsFactory;

import java.io.Serializable;

@Extension
public class FinalizeSnapshotVersionActionFactory extends InvisibleAction implements Serializable {

    @Inject
    private transient MultiProjectActionsFactory multiProjectActionsFactory;
    @Inject
    private transient ReleaseStepConfig releaseStepConfig;

    public FinalizeSnapshotVersionAction create() {
        return new FinalizeSnapshotVersionAction(
            multiProjectActionsFactory,
            releaseStepConfig.getScmActions()
        );
    }

}
