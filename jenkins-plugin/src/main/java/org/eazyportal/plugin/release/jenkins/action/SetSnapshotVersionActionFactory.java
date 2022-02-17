package org.eazyportal.plugin.release.jenkins.action;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.SetSnapshotVersionAction;
import org.eazyportal.plugin.release.core.project.ProjectActions;
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;
import org.eazyportal.plugin.release.jenkins.project.ProjectActionsProvider;

import java.io.File;
import java.io.Serializable;

@Extension
public class SetSnapshotVersionActionFactory extends InvisibleAction implements Serializable {

    @Inject
    private transient ProjectActionsProvider projectActionsProvider;
    @Inject
    private transient ReleaseStepConfig releaseStepConfig;
    @Inject
    private transient SnapshotVersionProvider snapshotVersionProvider;

    public SetSnapshotVersionAction create(File workingDir) {
        ProjectActions projectActions = projectActionsProvider.provide(workingDir);

        SetSnapshotVersionAction setSnapshotVersionAction = new SetSnapshotVersionAction(projectActions, snapshotVersionProvider);
        setSnapshotVersionAction.scmActions = releaseStepConfig.getScmActions();
        setSnapshotVersionAction.scmConfig = releaseStepConfig.getScmConfig();

        return setSnapshotVersionAction;
    }

}
