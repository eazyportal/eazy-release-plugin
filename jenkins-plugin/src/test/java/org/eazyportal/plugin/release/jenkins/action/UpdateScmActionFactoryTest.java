package org.eazyportal.plugin.release.jenkins.action;

import org.eazyportal.plugin.release.core.UpdateScmAction;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.core.scm.model.ScmConfig;
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

class UpdateScmActionFactoryTest {

    @Mock
    private transient ReleaseStepConfig releaseStepConfig;

    @InjectMocks
    private UpdateScmActionFactory underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_() {
        // GIVEN
        ScmActions scmActions = mock(ScmActions.class);

        // WHEN
        when(releaseStepConfig.getScmActions()).thenReturn(scmActions);
        when(releaseStepConfig.getScmConfig()).thenReturn(ScmConfig.getGIT_FLOW());

        // THEN
        UpdateScmAction actual = underTest.create();

        assertThat(actual.getScmActions()).isEqualTo(scmActions);
        assertThat(actual.getScmConfig()).isEqualTo(ScmConfig.getGIT_FLOW());

        verifyNoInteractions(scmActions);
        verify(releaseStepConfig).getScmActions();
        verify(releaseStepConfig).getScmConfig();
        verifyNoMoreInteractions(releaseStepConfig);
    }

}
