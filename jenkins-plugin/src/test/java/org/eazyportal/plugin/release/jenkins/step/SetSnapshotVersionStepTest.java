package org.eazyportal.plugin.release.jenkins.step;

import hudson.FilePath;
import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction;
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

class SetSnapshotVersionStepTest extends ReleaseStepBaseTest {

    @Mock
    private SetSnapshotVersionAction<FilePath> setSnapshotVersionAction;

    private SetSnapshotVersionStep underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        underTest = new SetSnapshotVersionStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        // WHEN
        when(run.getAction(ReleaseActionFactory.class)).thenReturn(releaseActionFactory);

        when(releaseActionFactory.create(SetSnapshotVersionAction.class, run, workspace, envVars, launcher, taskListener))
            .thenReturn(setSnapshotVersionAction);

        doNothing().when(setSnapshotVersionAction).execute();

        // THEN
        underTest.perform(run, workspace, envVars, launcher, taskListener);

        verifyNoInteractions(envVars, launcher, taskListener);
        verify(run).getAction(ReleaseActionFactory.class);
        verify(releaseActionFactory).create(SetSnapshotVersionAction.class, run, workspace, envVars, launcher, taskListener);
        verify(setSnapshotVersionAction).execute();
        verifyNoMoreInteractions(releaseActionFactory, run, setSnapshotVersionAction);
    }

}
