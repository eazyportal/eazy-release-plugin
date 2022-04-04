package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.action.UpdateScmAction;
import org.eazyportal.plugin.release.jenkins.action.UpdateScmActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class UpdateScmStepTest {

    private UpdateScmStep underTest;

    @BeforeEach
    void setUp() {
        underTest = new UpdateScmStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        FilePath workspace = new FilePath(new File(""));

        Run<?, ?> run = mock(Run.class);
        UpdateScmActionFactory updateScmActionFactory = mock(UpdateScmActionFactory.class);
        UpdateScmAction updateScmAction = mock(UpdateScmAction.class);

        // WHEN
        when(run.getAction(UpdateScmActionFactory.class)).thenReturn(updateScmActionFactory);
        when(updateScmActionFactory.create()).thenReturn(updateScmAction);
        doNothing().when(updateScmAction).execute(any(File.class));

        // THEN
        underTest.perform(run, workspace, mock(EnvVars.class), mock(Launcher.class), mock(TaskListener.class));

        verify(run).getAction(UpdateScmActionFactory.class);
        verify(updateScmActionFactory).create();
        verify(updateScmAction).execute(any(File.class));
        verifyNoMoreInteractions(run, updateScmAction, updateScmActionFactory);
    }

}
