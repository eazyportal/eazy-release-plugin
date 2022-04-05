package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.action.PrepareRepositoryForReleaseAction;
import org.eazyportal.plugin.release.jenkins.action.PrepareRepositoryForReleaseActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class PrepareRepositoryForReleaseStepTest {

    @TempDir
    private File workingDir;

    private PrepareRepositoryForReleaseStep underTest;

    @BeforeEach
    void setUp() {
        underTest = new PrepareRepositoryForReleaseStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        FilePath workspace = new FilePath(workingDir);

        Run<?, ?> run = mock(Run.class);

        PrepareRepositoryForReleaseActionFactory prepareRepositoryForReleaseActionFactory = mock(PrepareRepositoryForReleaseActionFactory.class);
        PrepareRepositoryForReleaseAction prepareRepositoryForReleaseAction = mock(PrepareRepositoryForReleaseAction.class);

        // WHEN
        when(run.getAction(PrepareRepositoryForReleaseActionFactory.class)).thenReturn(prepareRepositoryForReleaseActionFactory);
        when(prepareRepositoryForReleaseActionFactory.create()).thenReturn(prepareRepositoryForReleaseAction);

        // THEN
        underTest.perform(run, workspace, mock(EnvVars.class), mock(Launcher.class), mock(TaskListener.class));

        verify(run).getAction(PrepareRepositoryForReleaseActionFactory.class);
        verify(prepareRepositoryForReleaseActionFactory).create();
        verify(prepareRepositoryForReleaseAction).execute(workingDir);

        verifyNoMoreInteractions(run, prepareRepositoryForReleaseAction, prepareRepositoryForReleaseActionFactory);
    }

}
