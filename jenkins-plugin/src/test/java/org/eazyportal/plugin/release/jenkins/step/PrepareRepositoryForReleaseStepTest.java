package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.action.PrepareRepositoryForReleaseAction;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.jenkins.action.PrepareRepositoryForReleaseActionFactory;
import org.eazyportal.plugin.release.jenkins.scm.ScmActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
        Launcher launcher = mock(Launcher.class);
        TaskListener taskListener = mock(TaskListener.class);

        ScmActions scmActions = mock(ScmActions.class);
        ScmActionFactory scmActionFactory = mock(ScmActionFactory.class);

        PrepareRepositoryForReleaseActionFactory prepareRepositoryForReleaseActionFactory = mock(PrepareRepositoryForReleaseActionFactory.class);
        PrepareRepositoryForReleaseAction prepareRepositoryForReleaseAction = mock(PrepareRepositoryForReleaseAction.class);

        // WHEN
        when(run.getAction(ScmActionFactory.class)).thenReturn(scmActionFactory);
        when(scmActionFactory.create(launcher, taskListener)).thenReturn(scmActions);

        when(run.getAction(PrepareRepositoryForReleaseActionFactory.class)).thenReturn(prepareRepositoryForReleaseActionFactory);
        when(prepareRepositoryForReleaseActionFactory.create(scmActions)).thenReturn(prepareRepositoryForReleaseAction);

        // THEN
        underTest.perform(run, workspace, mock(EnvVars.class), launcher, taskListener);

        verifyNoInteractions(launcher, scmActions, taskListener);

        verify(run).getAction(ScmActionFactory.class);
        verify(scmActionFactory).create(launcher, taskListener);

        verify(run).getAction(PrepareRepositoryForReleaseActionFactory.class);
        verify(prepareRepositoryForReleaseActionFactory).create(scmActions);
        verify(prepareRepositoryForReleaseAction).execute(workingDir);

        verifyNoMoreInteractions(
            run, prepareRepositoryForReleaseAction, prepareRepositoryForReleaseActionFactory, scmActionFactory
        );
    }

}
