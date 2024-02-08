package org.eazyportal.plugin.release.jenkins.action;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.action.UpdateScmAction;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;

import java.io.Serializable;

@Extension
public class UpdateScmActionFactory extends InvisibleAction implements Serializable {

    @Inject
    private transient ReleaseStepConfig releaseStepConfig;

    public UpdateScmAction create(ScmActions scmActions) {
        return new UpdateScmAction(
            scmActions,
            releaseStepConfig.getScmConfig()
        );
    }

}
