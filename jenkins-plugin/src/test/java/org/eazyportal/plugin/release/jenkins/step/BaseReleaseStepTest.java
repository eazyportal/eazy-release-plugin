package org.eazyportal.plugin.release.jenkins.step;

import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.core.scm.model.ScmConfig;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfigAction;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseReleaseStepTest {

    protected ReleaseStepConfigAction createReleaseStepConfigActionMock() {
        ReleaseStepConfigAction releaseStepConfigAction = mock(ReleaseStepConfigAction.class);

        when(releaseStepConfigAction.getConventionalCommitTypes()).thenReturn(mock(List.class));
        when(releaseStepConfigAction.getScmActions()).thenReturn(mock(ScmActions.class));
        when(releaseStepConfigAction.getScmConfig()).thenReturn(mock(ScmConfig.class));

        return releaseStepConfigAction;
    }

}
