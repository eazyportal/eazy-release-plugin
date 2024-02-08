package org.eazyportal.plugin.release.jenkins.action;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;

import java.io.Serializable;

@Extension
public class SetSnapshotVersionActionFactory extends InvisibleAction implements Serializable {

    @Inject
    private transient ReleaseStepConfig releaseStepConfig;
    @Inject
    private transient SnapshotVersionProvider snapshotVersionProvider;

    public SetSnapshotVersionAction create(ScmActions scmActions) {
        return new SetSnapshotVersionAction(
            scmActions,
            releaseStepConfig.getScmConfig(),
            snapshotVersionProvider
        );
    }

}
