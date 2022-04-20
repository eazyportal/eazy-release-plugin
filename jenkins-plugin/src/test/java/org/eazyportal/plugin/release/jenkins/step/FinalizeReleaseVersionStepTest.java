package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.action.FinalizeReleaseVersionAction;
import org.eazyportal.plugin.release.core.model.ProjectDescriptor;
import org.eazyportal.plugin.release.jenkins.ProjectDescriptorFactory;
import org.eazyportal.plugin.release.jenkins.action.FinalizeReleaseVersionActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class FinalizeReleaseVersionStepTest {

    @TempDir
    private File workingDir;

    private FinalizeReleaseVersionStep underTest;

    @BeforeEach
    void setUp() {
        underTest = new FinalizeReleaseVersionStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        FilePath workspace = new FilePath(workingDir);

        Run<?, ?> run = mock(Run.class);

        ProjectDescriptor projectDescriptor = mock(ProjectDescriptor.class);
        ProjectDescriptorFactory projectDescriptorFactory = mock(ProjectDescriptorFactory.class);

        FinalizeReleaseVersionActionFactory finalizeReleaseVersionActionFactory = mock(FinalizeReleaseVersionActionFactory.class);
        FinalizeReleaseVersionAction finalizeReleaseVersionAction = mock(FinalizeReleaseVersionAction.class);

        // WHEN
        when(run.getAction(ProjectDescriptorFactory.class)).thenReturn(projectDescriptorFactory);
        when(projectDescriptorFactory.create(workingDir)).thenReturn(projectDescriptor);

        when(run.getAction(FinalizeReleaseVersionActionFactory.class)).thenReturn(finalizeReleaseVersionActionFactory);
        when(finalizeReleaseVersionActionFactory.create()).thenReturn(finalizeReleaseVersionAction);

        // THEN
        underTest.perform(run, workspace, mock(EnvVars.class), mock(Launcher.class), mock(TaskListener.class));

        verifyNoMoreInteractions(projectDescriptor);

        verify(run).getAction(ProjectDescriptorFactory.class);
        verify(projectDescriptorFactory).create(workingDir);

        verify(run).getAction(FinalizeReleaseVersionActionFactory.class);
        verify(finalizeReleaseVersionActionFactory).create();
        verify(finalizeReleaseVersionAction).execute(projectDescriptor);

        verifyNoMoreInteractions(run, projectDescriptorFactory, finalizeReleaseVersionAction, finalizeReleaseVersionActionFactory);
    }

}
