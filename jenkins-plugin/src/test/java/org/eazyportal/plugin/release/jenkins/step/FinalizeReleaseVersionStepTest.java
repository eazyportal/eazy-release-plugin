package org.eazyportal.plugin.release.jenkins.step;

import org.eazyportal.plugin.release.core.action.FinalizeReleaseVersionAction;
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

class FinalizeReleaseVersionStepTest extends ReleaseStepBaseTest {

    @Mock
    private FinalizeReleaseVersionAction finalizeReleaseVersionAction;

    private FinalizeReleaseVersionStep underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        underTest = new FinalizeReleaseVersionStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        // WHEN
        when(run.getAction(ReleaseActionFactory.class)).thenReturn(releaseActionFactory);

        when(releaseActionFactory.create(FinalizeReleaseVersionAction.class, run, workspace, envVars, launcher, taskListener))
            .thenReturn(finalizeReleaseVersionAction);

        doNothing().when(finalizeReleaseVersionAction).execute();

        // THEN
        underTest.perform(run, workspace, envVars, launcher, taskListener);

        verifyNoInteractions(envVars, launcher, taskListener);
        verify(run).getAction(ReleaseActionFactory.class);
        verify(releaseActionFactory).create(FinalizeReleaseVersionAction.class, run, workspace, envVars, launcher, taskListener);
        verify(finalizeReleaseVersionAction).execute();
        verifyNoMoreInteractions(finalizeReleaseVersionAction, run);
    }

}
