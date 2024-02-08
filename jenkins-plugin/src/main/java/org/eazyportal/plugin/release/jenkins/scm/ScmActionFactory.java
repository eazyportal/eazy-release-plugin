package org.eazyportal.plugin.release.jenkins.scm;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.InvisibleAction;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.scm.GitActions;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.jenkins.executor.JenkinsCliCommandExecutor;

import java.io.Serializable;

@Extension
public class ScmActionFactory extends InvisibleAction implements Serializable {

    public ScmActions create(Launcher launcher, TaskListener listener) {
        return new GitActions(new JenkinsCliCommandExecutor(launcher, listener));
    }

}
