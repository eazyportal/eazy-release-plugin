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
import org.eazyportal.plugin.release.core.SetReleaseVersionAction;
import org.eazyportal.plugin.release.core.project.ProjectActions;
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider;
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfigAction;
import org.eazyportal.plugin.release.jenkins.project.ProjectActionsProvider;
import org.jenkinsci.Symbol;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class SetReleaseVersionStep extends Builder implements SimpleBuildStep, Serializable {

    @DataBoundConstructor
    public SetReleaseVersionStep() {
        // ignore
    }

    @Override
    public void perform(@NotNull Run<?, ?> run, @NotNull FilePath workspace, @NotNull EnvVars env, @NotNull Launcher launcher, @NotNull TaskListener listener)
            throws InterruptedException, IOException {

        File workingDir = new File(workspace.toURI());

        ProjectActions projectActions = ProjectActionsProvider.provide(workingDir);

        ReleaseStepConfigAction releaseStepConfigAction = run.getAction(ReleaseStepConfigAction.class);

        SetReleaseVersionAction setReleaseVersionAction =
            new SetReleaseVersionAction(projectActions, new ReleaseVersionProvider(), new VersionIncrementProvider());

        setReleaseVersionAction.conventionalCommitTypes = releaseStepConfigAction.getConventionalCommitTypes();
        setReleaseVersionAction.scmActions = releaseStepConfigAction.getScmActions();
        setReleaseVersionAction.scmConfig = releaseStepConfigAction.getScmConfig();

        setReleaseVersionAction.execute(workingDir);
    }

    @Extension
    @Symbol("setReleaseVersion")
    public static final class SetReleaseVersionStepDescriptor extends BuildStepDescriptor<Builder> {

        @NotNull
        @Override
        public String getDisplayName() {
            return "Set release version";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

    }

}
