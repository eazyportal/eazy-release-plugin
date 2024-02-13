package org.eazyportal.plugin.release.jenkins.step;

import hudson.FilePath;
import org.eazyportal.plugin.release.core.action.PrepareRepositoryForReleaseAction;
import org.eazyportal.plugin.release.jenkins.action.ReleaseActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class PrepareRepositoryForReleaseStepTest extends ReleaseStepBaseTest {

    @Mock
    private PrepareRepositoryForReleaseAction<FilePath> prepareRepositoryForReleaseAction;

    private PrepareRepositoryForReleaseStep underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        underTest = new PrepareRepositoryForReleaseStep();
    }

    @Test
    void test_perform() throws Exception {
        // GIVEN
        // WHEN
        when(run.getAction(ReleaseActionFactory.class)).thenReturn(releaseActionFactory);

        when(releaseActionFactory.create(PrepareRepositoryForReleaseAction.class, run, workspace, envVars, launcher, taskListener))
            .thenReturn(prepareRepositoryForReleaseAction);

        doNothing().when(prepareRepositoryForReleaseAction).execute();

        // THEN
        underTest.perform(run, workspace, envVars, launcher, taskListener);

        verifyNoInteractions(envVars, launcher, taskListener);
        verify(run).getAction(ReleaseActionFactory.class);
        verify(releaseActionFactory).create(PrepareRepositoryForReleaseAction.class, run, workspace, envVars, launcher, taskListener);
        verify(prepareRepositoryForReleaseAction).execute();

        verifyNoMoreInteractions(prepareRepositoryForReleaseAction, releaseActionFactory, run);
    }

}
