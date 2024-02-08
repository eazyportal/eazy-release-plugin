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
import org.eazyportal.plugin.release.core.action.model.ActionContext;
import org.eazyportal.plugin.release.core.model.ProjectDescriptor;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.jenkins.ProjectDescriptorFactory;
import org.eazyportal.plugin.release.jenkins.action.ActionContextFactory;
import org.eazyportal.plugin.release.jenkins.action.UpdateScmActionFactory;
import org.eazyportal.plugin.release.jenkins.scm.ScmActionFactory;
import org.jenkinsci.Symbol;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class UpdateScmStep extends Builder implements SimpleBuildStep, Serializable {

    @DataBoundConstructor
    public UpdateScmStep() {
        // required by Jenkins
    }

    @Override
    public void perform(@NotNull Run<?, ?> run, @NotNull FilePath workspace, @NotNull EnvVars env, @NotNull Launcher launcher, @NotNull TaskListener listener)
            throws InterruptedException, IOException {

        File workingDir = new File(workspace.toURI());

        ScmActions scmActions = run.getAction(ScmActionFactory.class)
            .create(launcher, listener);

        ProjectDescriptor projectDescriptor = run.getAction(ProjectDescriptorFactory.class)
            .create(workingDir, scmActions);

        ActionContext actionContext = run.getAction(ActionContextFactory.class)
            .create(env);

        run.getAction(UpdateScmActionFactory.class)
            .create(scmActions)
            .execute(projectDescriptor, actionContext);
    }

    @Extension
    @Symbol("updateScm")
    public static final class UpdateScmStepDescriptor extends BuildStepDescriptor<Builder> {

        @NotNull
        @Override
        public String getDisplayName() {
            return "Update SCM";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

    }

}
