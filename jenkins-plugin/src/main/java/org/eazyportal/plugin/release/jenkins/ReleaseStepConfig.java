package org.eazyportal.plugin.release.jenkins;

import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType;
import org.eazyportal.plugin.release.core.scm.model.ScmConfig;

import java.io.Serializable;
import java.util.List;

public class ReleaseStepConfig extends InvisibleAction implements Serializable {

    private final transient List<ConventionalCommitType> conventionalCommitTypes = ConventionalCommitType.getDEFAULT_TYPES();
    private final transient ScmConfig scmConfig = ScmConfig.getGIT_FLOW();

    public List<ConventionalCommitType> getConventionalCommitTypes() {
        return conventionalCommitTypes;
    }

    public ScmConfig getScmConfig() {
        return scmConfig;
    }

}
