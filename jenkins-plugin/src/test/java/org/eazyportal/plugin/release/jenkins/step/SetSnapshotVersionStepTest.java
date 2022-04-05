package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction;
import org.eazyportal.plugin.release.core.model.ProjectDescriptor;
import org.eazyportal.plugin.release.jenkins.ProjectDescriptorFactory;
import org.eazyportal.plugin.release.jenkins.action.SetSnapshotVersionActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

        ProjectDescriptor projectDescriptor = mock(ProjectDescriptor.class);
        ProjectDescriptorFactory projectDescriptorFactory = mock(ProjectDescriptorFactory.class);

        SetSnapshotVersionAction setSnapshotVersionAction = mock(SetSnapshotVersionAction.class);
        SetSnapshotVersionActionFactory setSnapshotVersionActionFactory = mock(SetSnapshotVersionActionFactory.class);

        // WHEN
        when(run.getAction(ProjectDescriptorFactory.class)).thenReturn(projectDescriptorFactory);
        when(projectDescriptorFactory.create(workingDir)).thenReturn(projectDescriptor);

        when(run.getAction(SetSnapshotVersionActionFactory.class)).thenReturn(setSnapshotVersionActionFactory);
        when(setSnapshotVersionActionFactory.create()).thenReturn(setSnapshotVersionAction);

        // THEN
        underTest.perform(run, workspace, mock(EnvVars.class), mock(Launcher.class), mock(TaskListener.class));

        verifyNoMoreInteractions(projectDescriptor);

        verify(run).getAction(ProjectDescriptorFactory.class);
        verify(projectDescriptorFactory).create(workingDir);

        verify(run).getAction(SetSnapshotVersionActionFactory.class);
        verify(setSnapshotVersionActionFactory).create();
        verify(setSnapshotVersionAction).execute(projectDescriptor);

        verifyNoMoreInteractions(run, projectDescriptorFactory, setSnapshotVersionAction, setSnapshotVersionActionFactory);
    }

}
