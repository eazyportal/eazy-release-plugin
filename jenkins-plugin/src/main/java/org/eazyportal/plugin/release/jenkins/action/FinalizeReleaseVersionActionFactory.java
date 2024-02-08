package org.eazyportal.plugin.release.jenkins.action;

import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.action.FinalizeReleaseVersionAction;
import org.eazyportal.plugin.release.core.scm.ScmActions;

import java.io.Serializable;

@Extension
public class FinalizeReleaseVersionActionFactory extends InvisibleAction implements Serializable {

    public FinalizeReleaseVersionAction create(ScmActions scmActions) {
        return new FinalizeReleaseVersionAction(scmActions);
    }

}
