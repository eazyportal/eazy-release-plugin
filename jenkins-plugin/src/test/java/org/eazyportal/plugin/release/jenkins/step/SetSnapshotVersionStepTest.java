package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.FixtureValues;
import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction;
import org.eazyportal.plugin.release.core.model.ProjectDescriptor;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.jenkins.ProjectDescriptorFactory;
import org.eazyportal.plugin.release.jenkins.action.ActionContextFactory;
import org.eazyportal.plugin.release.jenkins.action.SetSnapshotVersionActionFactory;
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

class SetSnapshotVersionStepTest {

    @TempDir
    private File workingDir;

    private SetSnapshotVersionStep underTest;

    @BeforeEach
    void setUp() {
        underTest = new SetSnapshotVersionStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        FilePath workspace = new FilePath(workingDir);

        Run<?, ?> run = mock(Run.class);
        EnvVars envVars = mock(EnvVars.class);
        Launcher launcher = mock(Launcher.class);
        TaskListener taskListener = mock(TaskListener.class);

        ScmActions scmActions = mock(ScmActions.class);
        ScmActionFactory scmActionFactory = mock(ScmActionFactory.class);

        ProjectDescriptor projectDescriptor = mock(ProjectDescriptor.class);
        ProjectDescriptorFactory projectDescriptorFactory = mock(ProjectDescriptorFactory.class);

        ActionContextFactory actionContextFactory = mock(ActionContextFactory.class);

        SetSnapshotVersionAction setSnapshotVersionAction = mock(SetSnapshotVersionAction.class);
        SetSnapshotVersionActionFactory setSnapshotVersionActionFactory = mock(SetSnapshotVersionActionFactory.class);

        // WHEN
        when(run.getAction(ScmActionFactory.class)).thenReturn(scmActionFactory);
        when(scmActionFactory.create(launcher, taskListener)).thenReturn(scmActions);

        when(run.getAction(ProjectDescriptorFactory.class)).thenReturn(projectDescriptorFactory);
        when(projectDescriptorFactory.create(workingDir, scmActions)).thenReturn(projectDescriptor);

        when(run.getAction(ActionContextFactory.class)).thenReturn(actionContextFactory);
        when(actionContextFactory.create(envVars)).thenReturn(FixtureValues.getACTION_CONTEXT());

        when(run.getAction(SetSnapshotVersionActionFactory.class)).thenReturn(setSnapshotVersionActionFactory);
        when(setSnapshotVersionActionFactory.create(scmActions)).thenReturn(setSnapshotVersionAction);

        // THEN
        underTest.perform(run, workspace, envVars, launcher, taskListener);

        verifyNoInteractions(envVars, launcher, projectDescriptor, scmActions, taskListener);

        verify(run).getAction(ScmActionFactory.class);
        verify(scmActionFactory).create(launcher, taskListener);

        verify(run).getAction(ProjectDescriptorFactory.class);
        verify(projectDescriptorFactory).create(workingDir, scmActions);

        verify(run).getAction(ActionContextFactory.class);
        verify(actionContextFactory).create(envVars);

        verify(run).getAction(SetSnapshotVersionActionFactory.class);
        verify(setSnapshotVersionActionFactory).create(scmActions);
        verify(setSnapshotVersionAction).execute(projectDescriptor, FixtureValues.getACTION_CONTEXT());

        verifyNoMoreInteractions(
            actionContextFactory, projectDescriptorFactory, run, scmActionFactory, setSnapshotVersionAction,
            setSnapshotVersionActionFactory
        );
    }

}
