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
import org.eazyportal.plugin.release.core.action.PrepareRepositoryForReleaseAction;
import org.eazyportal.plugin.release.jenkins.action.ReleaseActionFactory;
import org.jenkinsci.Symbol;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.Serializable;

public class PrepareRepositoryForReleaseStep extends Builder implements SimpleBuildStep, Serializable {

    @DataBoundConstructor
    public PrepareRepositoryForReleaseStep() {
        // required by Jenkins
    }

    @Override
    public void perform(@NotNull Run<?, ?> run, @NotNull FilePath workspace, @NotNull EnvVars env, @NotNull Launcher launcher, @NotNull TaskListener listener)
        throws InterruptedException, IOException {

        run.getAction(ReleaseActionFactory.class)
            .create(PrepareRepositoryForReleaseAction.class, run, workspace, env, launcher, listener)
            .execute();
    }

    @Extension
    @Symbol("prepareRepositoryForRelease")
    public static final class PrepareRepositoryForReleaseStepDescriptor extends BuildStepDescriptor<Builder> {

        @NotNull
        @Override
        public String getDisplayName() {
            return "Prepare repository for release";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

    }

}
