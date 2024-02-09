package org.eazyportal.plugin.release.jenkins.step;

import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction;
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

class FinalizeSnapshotVersionStepTest extends ReleaseStepBaseTest {

    @Mock
    private FinalizeSnapshotVersionAction finalizeSnapshotVersionAction;

    private FinalizeSnapshotVersionStep underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        underTest = new FinalizeSnapshotVersionStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        // WHEN
        when(run.getAction(ReleaseActionFactory.class)).thenReturn(releaseActionFactory);

        when(releaseActionFactory.create(FinalizeSnapshotVersionAction.class, run, workingDir, envVars, launcher, taskListener))
            .thenReturn(finalizeSnapshotVersionAction);

        doNothing().when(finalizeSnapshotVersionAction).execute();

        // THEN
        underTest.perform(run, workspace, envVars, launcher, taskListener);

        verifyNoInteractions(envVars, launcher, taskListener);
        verify(run).getAction(ReleaseActionFactory.class);
        verify(releaseActionFactory).create(FinalizeSnapshotVersionAction.class, run, workingDir, envVars, launcher, taskListener);
        verify(finalizeSnapshotVersionAction).execute();
        verifyNoMoreInteractions(finalizeSnapshotVersionAction, releaseActionFactory, run);
    }

}
