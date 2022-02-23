package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.SetReleaseVersionAction;
import org.eazyportal.plugin.release.jenkins.action.SetReleaseVersionActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class SetReleaseVersionStepTest {

    private SetReleaseVersionStep underTest;

    @BeforeEach
    void setUp() {
        underTest = new SetReleaseVersionStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        FilePath workspace = new FilePath(new File(""));

        Run<?, ?> run = mock(Run.class);
        SetReleaseVersionActionFactory setReleaseVersionActionFactory = mock(SetReleaseVersionActionFactory.class);
        SetReleaseVersionAction setReleaseVersionAction = mock(SetReleaseVersionAction.class);

        // WHEN
        when(run.getAction(SetReleaseVersionActionFactory.class)).thenReturn(setReleaseVersionActionFactory);
        when(setReleaseVersionActionFactory.create()).thenReturn(setReleaseVersionAction);
        doNothing().when(setReleaseVersionAction).execute(any(File.class));

        // THEN
        underTest.perform(run, workspace, mock(EnvVars.class), mock(Launcher.class), mock(TaskListener.class));

        verify(run).getAction(SetReleaseVersionActionFactory.class);
        verify(setReleaseVersionActionFactory).create();
        verify(setReleaseVersionAction).execute(any(File.class));
        verifyNoMoreInteractions(run, setReleaseVersionAction, setReleaseVersionActionFactory);
    }

}
