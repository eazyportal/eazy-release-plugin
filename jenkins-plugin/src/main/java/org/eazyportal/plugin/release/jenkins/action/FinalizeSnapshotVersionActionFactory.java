package org.eazyportal.plugin.release.jenkins.action;

import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction;
import org.eazyportal.plugin.release.core.scm.ScmActions;

import java.io.Serializable;

@Extension
public class FinalizeSnapshotVersionActionFactory extends InvisibleAction implements Serializable {

    public FinalizeSnapshotVersionAction create(ScmActions scmActions) {
        return new FinalizeSnapshotVersionAction(scmActions);
    }

}
