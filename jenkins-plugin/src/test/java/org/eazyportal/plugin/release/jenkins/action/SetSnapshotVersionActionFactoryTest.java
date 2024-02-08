package org.eazyportal.plugin.release.jenkins.action;

import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.core.scm.model.ScmConfig;
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;
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

class SetSnapshotVersionActionFactoryTest {

    @Mock
    private transient ReleaseStepConfig releaseStepConfig;
    @Mock
    private transient SnapshotVersionProvider snapshotVersionProvider;

    @InjectMocks
    private SetSnapshotVersionActionFactory underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_create() {
        // GIVEN
        ScmActions scmActions = mock(ScmActions.class);

        // WHEN
        when(releaseStepConfig.getScmConfig()).thenReturn(ScmConfig.getGIT_FLOW());

        // THEN
        SetSnapshotVersionAction actual = underTest.create(scmActions);

        assertThat(actual).hasNoNullFieldsOrProperties();

        verifyNoInteractions(scmActions, snapshotVersionProvider);
        verify(releaseStepConfig).getScmConfig();
        verifyNoMoreInteractions(releaseStepConfig);
    }

}
