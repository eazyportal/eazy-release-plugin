package org.eazyportal.plugin.release.jenkins;

import hudson.model.Run;
import org.eazyportal.plugin.release.jenkins.action.ActionContextFactory;
import org.eazyportal.plugin.release.jenkins.action.PrepareRepositoryForReleaseActionFactory;
import org.eazyportal.plugin.release.jenkins.action.ReleaseActionFactory;
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
    private PrepareRepositoryForReleaseActionFactory prepareRepositoryForReleaseActionFactory;
    @Mock
    private ProjectDescriptorFactory projectDescriptorFactory;
    @Mock
    private ReleaseActionFactory releaseActionFactory;
    @Mock
    private ScmActionFactory scmActionFactory;

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
        verify(run).addAction(prepareRepositoryForReleaseActionFactory);
        verify(run).addAction(projectDescriptorFactory);
        verify(run).addAction(releaseActionFactory);
        verify(run).addAction(scmActionFactory);
        verifyNoMoreInteractions(run);
    }

}
