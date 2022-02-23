package org.eazyportal.plugin.release.jenkins.action;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.SetReleaseVersionAction;
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider;
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;
import org.eazyportal.plugin.release.jenkins.project.MultiProjectActionsFactory;

import java.io.Serializable;

@Extension
public class SetReleaseVersionActionFactory extends InvisibleAction implements Serializable {

    @Inject
    private transient MultiProjectActionsFactory multiProjectActionsFactory;
    @Inject
    private transient ReleaseStepConfig releaseStepConfig;
    @Inject
    private transient ReleaseVersionProvider releaseVersionProvider;
    @Inject
    private transient VersionIncrementProvider versionIncrementProvider;

    public SetReleaseVersionAction create() {
        SetReleaseVersionAction setReleaseVersionAction =
            new SetReleaseVersionAction(multiProjectActionsFactory, releaseVersionProvider, versionIncrementProvider);

        setReleaseVersionAction.conventionalCommitTypes = releaseStepConfig.getConventionalCommitTypes();
        setReleaseVersionAction.scmActions = releaseStepConfig.getScmActions();
        setReleaseVersionAction.scmConfig = releaseStepConfig.getScmConfig();

        return setReleaseVersionAction;
    }

}
