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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eazyportal.plugin.release.core.SetReleaseVersionAction;
import org.eazyportal.plugin.release.core.executor.CliCommandExecutor;
import org.eazyportal.plugin.release.core.project.ProjectActions;
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType;
import org.eazyportal.plugin.release.core.scm.GitActions;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.core.scm.model.ScmConfig;
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider;
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider;
import org.eazyportal.plugin.release.jenkins.project.ProjectActionsProvider;
import org.jenkinsci.Symbol;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class SetReleaseVersionStep extends Builder implements SimpleBuildStep, Serializable {

    private final transient ScmActions scmActions;

    // TODO: fix getters and setters to be able to configure from the UI/pipeline
    private transient List<ConventionalCommitType> conventionalCommitTypes;
    private transient ScmConfig scmConfig;

    @DataBoundConstructor
    public SetReleaseVersionStep() {
        conventionalCommitTypes = ConventionalCommitType.getDEFAULT_TYPES();
        scmActions = new GitActions(new CliCommandExecutor());
        scmConfig = ScmConfig.getGIT_FLOW();
    }

    public List<String> getConventionalCommitTypes() {
        return JSONArray.fromObject(conventionalCommitTypes)
            .stream()
            .map(Object::toString)
            .collect(Collectors.toList());
    }

    //@DataBoundSetter
    public void setConventionalCommitTypes(List<ConventionalCommitType> conventionalCommitTypes) {
        this.conventionalCommitTypes = conventionalCommitTypes;
    }

    public String getScmConfig() {
        return JSONObject.fromObject(scmConfig)
            .toString();
    }

    //@DataBoundSetter
    public void setScmConfig(ScmConfig scmConfig) {
        this.scmConfig = scmConfig;
    }

    @Override
    public void perform(@NotNull Run<?, ?> run, @NotNull FilePath workspace, @NotNull EnvVars env, @NotNull Launcher launcher, @NotNull TaskListener listener)
            throws InterruptedException, IOException {

        File workingDir = new File(workspace.toURI());

        ProjectActions projectActions = ProjectActionsProvider.provide(workingDir);

        SetReleaseVersionAction setReleaseVersionAction =
                new SetReleaseVersionAction(projectActions, new ReleaseVersionProvider(), new VersionIncrementProvider());

        setReleaseVersionAction.conventionalCommitTypes = conventionalCommitTypes;
        setReleaseVersionAction.scmActions = scmActions;
        setReleaseVersionAction.scmConfig = scmConfig;

        setReleaseVersionAction.execute(workingDir);
    }

    @Extension
    @Symbol("setReleaseVersion")
    public static final class SetReleaseVersionStepDescriptor extends BuildStepDescriptor<Builder> {

        public SetReleaseVersionStepDescriptor() {
            super(SetReleaseVersionStep.class);
        }

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
