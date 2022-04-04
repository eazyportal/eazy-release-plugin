package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction;
import org.eazyportal.plugin.release.jenkins.action.SetSnapshotVersionActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class SetSnapshotVersionStepTest {

    private SetSnapshotVersionStep underTest;

    @BeforeEach
    void setUp() {
        underTest = new SetSnapshotVersionStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        FilePath workspace = new FilePath(new File(""));

        Run<?, ?> run = mock(Run.class);
        SetSnapshotVersionActionFactory setSnapshotVersionActionFactory = mock(SetSnapshotVersionActionFactory.class);
        SetSnapshotVersionAction setSnapshotVersionAction = mock(SetSnapshotVersionAction.class);

        // WHEN
        when(run.getAction(SetSnapshotVersionActionFactory.class)).thenReturn(setSnapshotVersionActionFactory);
        when(setSnapshotVersionActionFactory.create()).thenReturn(setSnapshotVersionAction);
        doNothing().when(setSnapshotVersionAction).execute(any(File.class));

        // THEN
        underTest.perform(run, workspace, mock(EnvVars.class), mock(Launcher.class), mock(TaskListener.class));

        verify(run).getAction(SetSnapshotVersionActionFactory.class);
        verify(setSnapshotVersionActionFactory).create();
        verify(setSnapshotVersionAction).execute(any(File.class));
        verifyNoMoreInteractions(run, setSnapshotVersionAction, setSnapshotVersionActionFactory);
    }

}
