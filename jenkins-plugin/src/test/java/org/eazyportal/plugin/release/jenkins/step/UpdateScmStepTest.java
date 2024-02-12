package org.eazyportal.plugin.release.jenkins.step;

import hudson.FilePath;
import org.eazyportal.plugin.release.core.action.UpdateScmAction;
import org.eazyportal.plugin.release.jenkins.action.ReleaseActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class UpdateScmStepTest extends ReleaseStepBaseTest {

    @Mock
    private UpdateScmAction<FilePath> updateScmAction;

    private UpdateScmStep underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        underTest = new UpdateScmStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        // WHEN
        when(run.getAction(ReleaseActionFactory.class)).thenReturn(releaseActionFactory);

        when(releaseActionFactory.create(UpdateScmAction.class, run, workspace, envVars, launcher, taskListener))
            .thenReturn(updateScmAction);

        doNothing().when(updateScmAction).execute();

        // THEN
        underTest.perform(run, workspace, envVars, launcher, taskListener);

        verifyNoInteractions(envVars, launcher, taskListener);
        verify(run).getAction(ReleaseActionFactory.class);
        verify(releaseActionFactory).create(UpdateScmAction.class, run, workspace, envVars, launcher, taskListener);
        verify(updateScmAction).execute();

        verifyNoMoreInteractions(releaseActionFactory, run, updateScmAction);
    }

}
