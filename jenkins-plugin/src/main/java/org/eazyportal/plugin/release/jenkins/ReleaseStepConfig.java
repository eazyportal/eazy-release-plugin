package org.eazyportal.plugin.release.jenkins;

import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType;
import org.eazyportal.plugin.release.core.scm.JGitActions;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.core.scm.model.ScmConfig;

import java.io.Serializable;
import java.util.List;

public class ReleaseStepConfig extends InvisibleAction implements Serializable {

    private final transient List<ConventionalCommitType> conventionalCommitTypes = ConventionalCommitType.getDEFAULT_TYPES();
    private final transient ScmActions scmActions = new JGitActions();
    private final transient ScmConfig scmConfig = ScmConfig.getGIT_FLOW();

    public List<ConventionalCommitType> getConventionalCommitTypes() {
        return conventionalCommitTypes;
    }

    public ScmActions getScmActions() {
        return scmActions;
    }

    public ScmConfig getScmConfig() {
        return scmConfig;
    }

}
