package org.eazyportal.plugin.release.jenkins.action;

import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;
import org.eazyportal.plugin.release.jenkins.project.MultiProjectActionsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class FinalizeSnapshotVersionActionFactoryTest {

    @Mock
    private transient MultiProjectActionsFactory multiProjectActionsFactory;
    @Mock
    private transient ReleaseStepConfig releaseStepConfig;

    @InjectMocks
    private FinalizeSnapshotVersionActionFactory underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_create() {
        // GIVEN
        ScmActions scmActions = mock(ScmActions.class);

        // WHEN
        when(releaseStepConfig.getScmActions()).thenReturn(scmActions);

        // THEN
        FinalizeSnapshotVersionAction actual = underTest.create();

        assertThat(actual).hasNoNullFieldsOrProperties();

        verifyNoInteractions(multiProjectActionsFactory, scmActions);
        verify(releaseStepConfig).getScmActions();
        verifyNoMoreInteractions(releaseStepConfig);
    }

}
