package org.eazyportal.plugin.release.jenkins;

import hudson.model.Run;
import hudson.model.TaskListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class ReleasePluginRunListenerTest {

    private ReleasePluginRunListener underTest;

    @BeforeEach
    void setUp() {
        underTest = new ReleasePluginRunListener();
    }

    @Test
    void test_onStarted() {
        // GIVEN
        Run<?, ?> run = mock(Run.class);
        TaskListener taskListener = mock(TaskListener.class);
        ArgumentCaptor<ReleaseStepConfigAction> releaseStepConfigActionArgumentCaptor = ArgumentCaptor.forClass(ReleaseStepConfigAction.class);

        // WHEN
        doNothing().when(run).addAction(releaseStepConfigActionArgumentCaptor.capture());

        // THEN
        underTest.onStarted(run, taskListener);

        verify(run).addAction(releaseStepConfigActionArgumentCaptor.capture());
        verifyNoMoreInteractions(run);

        assertThat(releaseStepConfigActionArgumentCaptor.getValue()).isNotNull();
    }

}
