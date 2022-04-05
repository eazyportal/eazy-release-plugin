package org.eazyportal.plugin.release.jenkins.action;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.action.PrepareRepositoryForReleaseAction;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;

import java.io.Serializable;

@Extension
public class PrepareRepositoryForReleaseActionFactory extends InvisibleAction implements Serializable {

    @Inject
    private transient ReleaseStepConfig releaseStepConfig;

    public PrepareRepositoryForReleaseAction create() {
        return new PrepareRepositoryForReleaseAction(
            releaseStepConfig.getScmActions(),
            releaseStepConfig.getScmConfig()
        );
    }

}
