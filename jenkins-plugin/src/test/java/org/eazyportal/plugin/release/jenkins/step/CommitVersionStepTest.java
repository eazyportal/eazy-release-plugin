package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.action.FinalizeReleaseVersionAction;
import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction;
import org.eazyportal.plugin.release.core.model.ProjectDescriptor;
import org.eazyportal.plugin.release.jenkins.ProjectDescriptorFactory;
import org.eazyportal.plugin.release.jenkins.action.FinalizeReleaseVersionActionFactory;
import org.eazyportal.plugin.release.jenkins.action.FinalizeSnapshotVersionActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Answers;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CommitVersionStepTest {

    @TempDir
    private File workingDir;

    private CommitVersionStep underTest;

    @BeforeEach
    void setUp() {
        underTest = new CommitVersionStep();
    }

    @Test
    void test_perform_whenReleaseVersion() throws Exception {
        // GIVEN
        FilePath workspace = new FilePath(workingDir);

        Run<?, ?> run = mock(Run.class);

        ProjectDescriptor projectDescriptor = mock(ProjectDescriptor.class, Answers.RETURNS_DEEP_STUBS);
        ProjectDescriptorFactory projectDescriptorFactory = mock(ProjectDescriptorFactory.class);

        FinalizeReleaseVersionActionFactory finalizeReleaseVersionActionFactory = mock(FinalizeReleaseVersionActionFactory.class);
        FinalizeReleaseVersionAction finalizeReleaseVersionAction = mock(FinalizeReleaseVersionAction.class);

        // WHEN
        when(run.getAction(ProjectDescriptorFactory.class)).thenReturn(projectDescriptorFactory);
        when(projectDescriptorFactory.create(workingDir)).thenReturn(projectDescriptor);
        when(projectDescriptor.getRootProject().getProjectActions().getVersion().isRelease()).thenReturn(true);

        when(run.getAction(FinalizeReleaseVersionActionFactory.class)).thenReturn(finalizeReleaseVersionActionFactory);
        when(finalizeReleaseVersionActionFactory.create()).thenReturn(finalizeReleaseVersionAction);

        // THEN
        underTest.perform(run, workspace, mock(EnvVars.class), mock(Launcher.class), mock(TaskListener.class));

        verify(run).getAction(ProjectDescriptorFactory.class);
        verify(projectDescriptorFactory).create(workingDir);
        verify(projectDescriptor.getRootProject().getProjectActions().getVersion()).isRelease();

        verify(run).getAction(FinalizeReleaseVersionActionFactory.class);
        verify(finalizeReleaseVersionActionFactory).create();
        verify(finalizeReleaseVersionAction).execute(projectDescriptor);

        verifyNoMoreInteractions(run, projectDescriptorFactory, finalizeReleaseVersionAction, finalizeReleaseVersionActionFactory);
    }

    @Test
    void test_perform_whenSnapshotVersion() throws Exception {
        // GIVEN
        FilePath workspace = new FilePath(workingDir);

        Run<?, ?> run = mock(Run.class);

        ProjectDescriptor projectDescriptor = mock(ProjectDescriptor.class, Answers.RETURNS_DEEP_STUBS);
        ProjectDescriptorFactory projectDescriptorFactory = mock(ProjectDescriptorFactory.class);

        FinalizeSnapshotVersionActionFactory finalizeSnapshotVersionActionFactory = mock(FinalizeSnapshotVersionActionFactory.class);
        FinalizeSnapshotVersionAction finalizeSnapshotVersionAction = mock(FinalizeSnapshotVersionAction.class);

        // WHEN
        when(run.getAction(ProjectDescriptorFactory.class)).thenReturn(projectDescriptorFactory);
        when(projectDescriptorFactory.create(workingDir)).thenReturn(projectDescriptor);
        when(projectDescriptor.getRootProject().getProjectActions().getVersion().isRelease()).thenReturn(false);

        when(run.getAction(FinalizeSnapshotVersionActionFactory.class)).thenReturn(finalizeSnapshotVersionActionFactory);
        when(finalizeSnapshotVersionActionFactory.create()).thenReturn(finalizeSnapshotVersionAction);

        // THEN
        underTest.perform(run, workspace, mock(EnvVars.class), mock(Launcher.class), mock(TaskListener.class));

        verify(run).getAction(ProjectDescriptorFactory.class);
        verify(projectDescriptorFactory).create(workingDir);
        verify(projectDescriptor.getRootProject().getProjectActions().getVersion()).isRelease();

        verify(run).getAction(FinalizeSnapshotVersionActionFactory.class);
        verify(finalizeSnapshotVersionActionFactory).create();
        verify(finalizeSnapshotVersionAction).execute(projectDescriptor);

        verifyNoMoreInteractions(run, projectDescriptorFactory, finalizeSnapshotVersionAction, finalizeSnapshotVersionActionFactory);
    }

}
