package org.eazyportal.plugin.release.jenkins;

import hudson.model.Run;
import org.eazyportal.plugin.release.jenkins.action.ActionContextFactory;
import org.eazyportal.plugin.release.jenkins.action.FinalizeReleaseVersionActionFactory;
import org.eazyportal.plugin.release.jenkins.action.FinalizeSnapshotVersionActionFactory;
import org.eazyportal.plugin.release.jenkins.action.PrepareRepositoryForReleaseActionFactory;
import org.eazyportal.plugin.release.jenkins.action.SetReleaseVersionActionFactory;
import org.eazyportal.plugin.release.jenkins.action.SetSnapshotVersionActionFactory;
import org.eazyportal.plugin.release.jenkins.action.UpdateScmActionFactory;
import org.eazyportal.plugin.release.jenkins.scm.ScmActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class ReleasePluginRunListenerTest {

    @Mock
    private ActionContextFactory actionContextFactory;
    @Mock
    private FinalizeReleaseVersionActionFactory finalizeReleaseVersionActionFactory;
    @Mock
    private FinalizeSnapshotVersionActionFactory finalizeSnapshotVersionActionFactory;
    @Mock
    private PrepareRepositoryForReleaseActionFactory prepareRepositoryForReleaseActionFactory;
    @Mock
    private ProjectDescriptorFactory projectDescriptorFactory;
    @Mock
    private ScmActionFactory scmActionFactory;
    @Mock
    private SetReleaseVersionActionFactory setReleaseVersionActionFactory;
    @Mock
    private SetSnapshotVersionActionFactory setSnapshotVersionActionFactory;
    @Mock
    private UpdateScmActionFactory updateScmActionFactory;

    @InjectMocks
    private ReleasePluginRunListener underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_onInitialize() {
        // GIVEN
        Run<?, ?> run = mock(Run.class);

        // WHEN
        doNothing().when(run).addAction(any());

        // THEN
        underTest.onInitialize(run);

        verify(run).addAction(actionContextFactory);
        verify(run).addAction(finalizeReleaseVersionActionFactory);
        verify(run).addAction(finalizeSnapshotVersionActionFactory);
        verify(run).addAction(prepareRepositoryForReleaseActionFactory);
        verify(run).addAction(projectDescriptorFactory);
        verify(run).addAction(scmActionFactory);
        verify(run).addAction(setReleaseVersionActionFactory);
        verify(run).addAction(setSnapshotVersionActionFactory);
        verify(run).addAction(updateScmActionFactory);
        verifyNoMoreInteractions(run);
    }

}
