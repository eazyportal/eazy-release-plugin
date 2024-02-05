package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.FixtureValues;
import org.eazyportal.plugin.release.core.action.UpdateScmAction;
import org.eazyportal.plugin.release.core.model.ProjectDescriptor;
import org.eazyportal.plugin.release.jenkins.ProjectDescriptorFactory;
import org.eazyportal.plugin.release.jenkins.action.ActionContextFactory;
import org.eazyportal.plugin.release.jenkins.action.UpdateScmActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class UpdateScmStepTest {

    @TempDir
    private File workingDir;

    private UpdateScmStep underTest;

    @BeforeEach
    void setUp() {
        underTest = new UpdateScmStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        FilePath workspace = new FilePath(workingDir);

        Run<?, ?> run = mock(Run.class);
        EnvVars envVars = mock(EnvVars.class);

        ProjectDescriptor projectDescriptor = mock(ProjectDescriptor.class);
        ProjectDescriptorFactory projectDescriptorFactory = mock(ProjectDescriptorFactory.class);

        ActionContextFactory actionContextFactory = mock(ActionContextFactory.class);

        UpdateScmActionFactory updateScmActionFactory = mock(UpdateScmActionFactory.class);
        UpdateScmAction updateScmAction = mock(UpdateScmAction.class);

        // WHEN
        when(run.getAction(ProjectDescriptorFactory.class)).thenReturn(projectDescriptorFactory);
        when(projectDescriptorFactory.create(workingDir)).thenReturn(projectDescriptor);

        when(run.getAction(ActionContextFactory.class)).thenReturn(actionContextFactory);
        when(actionContextFactory.create(envVars)).thenReturn(FixtureValues.getACTION_CONTEXT());

        when(run.getAction(UpdateScmActionFactory.class)).thenReturn(updateScmActionFactory);
        when(updateScmActionFactory.create()).thenReturn(updateScmAction);

        // THEN
        underTest.perform(run, workspace, envVars, mock(Launcher.class), mock(TaskListener.class));

        verifyNoMoreInteractions(projectDescriptor);

        verify(run).getAction(ProjectDescriptorFactory.class);
        verify(projectDescriptorFactory).create(workingDir);

        verify(run).getAction(ActionContextFactory.class);
        verify(actionContextFactory).create(envVars);

        verify(run).getAction(UpdateScmActionFactory.class);
        verify(updateScmActionFactory).create();
        verify(updateScmAction).execute(projectDescriptor, FixtureValues.getACTION_CONTEXT());

        verifyNoMoreInteractions(run, projectDescriptorFactory, updateScmAction, updateScmActionFactory);
    }

}
