package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfigAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class SetSnapshotVersionStepTest extends BaseReleaseStepTest {

    private SetSnapshotVersionStep underTest;

    @BeforeEach
    void setUp() {
        underTest = new SetSnapshotVersionStep();
    }

    @Disabled("Tested class uses ProjectActionsProvider which cannot be mocked and SetSnapshotVersionAction is instantiated inside causes issues.")
    @Test
    void test_perform() throws Exception {
        Run<?, ?> run = mock(Run.class);
        FilePath workspace = new FilePath(new File(""));
        ReleaseStepConfigAction releaseStepConfigAction = createReleaseStepConfigActionMock();

        // WHEN
        when(run.getAction(ReleaseStepConfigAction.class)).thenReturn(releaseStepConfigAction);

        // THEN
        underTest.perform(run, workspace, mock(EnvVars.class), mock(Launcher.class), mock(TaskListener.class));

        verify(releaseStepConfigAction).getScmActions();
        verify(releaseStepConfigAction).getScmConfig();
        verifyNoMoreInteractions(releaseStepConfigAction);
    }

}
