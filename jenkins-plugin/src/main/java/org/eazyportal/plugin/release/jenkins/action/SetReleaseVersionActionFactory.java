package org.eazyportal.plugin.release.jenkins.action;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.action.SetReleaseVersionAction;
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider;
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;

import java.io.Serializable;

@Extension
public class SetReleaseVersionActionFactory extends InvisibleAction implements Serializable {

    @Inject
    private transient ReleaseStepConfig releaseStepConfig;
    @Inject
    private transient ReleaseVersionProvider releaseVersionProvider;
    @Inject
    private transient VersionIncrementProvider versionIncrementProvider;

    public SetReleaseVersionAction create() {
        return new SetReleaseVersionAction(
            releaseStepConfig.getConventionalCommitTypes(),
            releaseVersionProvider,
            releaseStepConfig.getScmActions(),
            releaseStepConfig.getScmConfig(),
            versionIncrementProvider
        );
    }

}
