package org.eazyportal.plugin.release.jenkins.step;

import hudson.FilePath;
import org.eazyportal.plugin.release.core.action.SetReleaseVersionAction;
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

class SetReleaseVersionStepTest extends ReleaseStepBaseTest {

    @Mock
    private SetReleaseVersionAction<FilePath> setReleaseVersionAction;

    private SetReleaseVersionStep underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        underTest = new SetReleaseVersionStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        // WHEN
        when(run.getAction(ReleaseActionFactory.class)).thenReturn(releaseActionFactory);

        when(releaseActionFactory.create(SetReleaseVersionAction.class, run, workspace, envVars, launcher, taskListener))
            .thenReturn(setReleaseVersionAction);

        doNothing().when(setReleaseVersionAction).execute();

        // THEN
        underTest.perform(run, workspace, envVars, launcher, taskListener);

        verifyNoInteractions(envVars, launcher, taskListener);
        verify(run).getAction(ReleaseActionFactory.class);
        verify(releaseActionFactory).create(SetReleaseVersionAction.class, run, workspace, envVars, launcher, taskListener);
        verify(setReleaseVersionAction).execute();
        verifyNoMoreInteractions(releaseActionFactory, run, setReleaseVersionAction);
    }

}
