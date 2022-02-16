package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import org.eazyportal.plugin.release.core.SetSnapshotVersionAction;
import org.eazyportal.plugin.release.core.project.ProjectActions;
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfigAction;
import org.eazyportal.plugin.release.jenkins.project.ProjectActionsProvider;
import org.jenkinsci.Symbol;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class SetSnapshotVersionStep extends Builder implements SimpleBuildStep, Serializable {

    @DataBoundConstructor
    public SetSnapshotVersionStep() {
        // ignore
    }

    @Override
    public void perform(@NotNull Run<?, ?> run, @NotNull FilePath workspace, @NotNull EnvVars env, @NotNull Launcher launcher, @NotNull TaskListener listener)
            throws InterruptedException, IOException {

        File workingDir = new File(workspace.toURI());

        ProjectActions projectActions = ProjectActionsProvider.provide(workingDir);

        ReleaseStepConfigAction releaseStepConfigAction = run.getAction(ReleaseStepConfigAction.class);

        SetSnapshotVersionAction setSnapshotVersionAction = new SetSnapshotVersionAction(projectActions, new SnapshotVersionProvider());
        setSnapshotVersionAction.scmActions = releaseStepConfigAction.getScmActions();
        setSnapshotVersionAction.scmConfig = releaseStepConfigAction.getScmConfig();

        setSnapshotVersionAction.execute(workingDir);
    }

    @Extension
    @Symbol("setSnapshotVersion")
    public static final class SetSnapshotVersionStepDescriptor extends BuildStepDescriptor<Builder> {

        @NotNull
        @Override
        public String getDisplayName() {
            return "Set snapshot version";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

    }

}
