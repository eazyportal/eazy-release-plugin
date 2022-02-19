package org.eazyportal.plugin.release.jenkins.action;

import org.eazyportal.plugin.release.core.SetReleaseVersionAction;
import org.eazyportal.plugin.release.core.project.ProjectActions;
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.core.scm.model.ScmConfig;
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider;
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;
import org.eazyportal.plugin.release.jenkins.project.ProjectActionsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class SetReleaseVersionActionFactoryTest {

    @Mock
    private transient ProjectActionsProvider projectActionsProvider;
    @Mock
    private transient ReleaseStepConfig releaseStepConfig;
    @Mock
    private transient ReleaseVersionProvider releaseVersionProvider;
    @Mock
    private transient VersionIncrementProvider versionIncrementProvider;

    @InjectMocks
    private SetReleaseVersionActionFactory underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_create() {
        // GIVEN
        File workingDir = new File("");

        ProjectActions projectActions = mock(ProjectActions.class);
        ScmActions scmActions = mock(ScmActions.class);

        // WHEN
        when(projectActionsProvider.provide(workingDir)).thenReturn(projectActions);
        when(releaseStepConfig.getConventionalCommitTypes()).thenReturn(ConventionalCommitType.getDEFAULT_TYPES());
        when(releaseStepConfig.getScmActions()).thenReturn(scmActions);
        when(releaseStepConfig.getScmConfig()).thenReturn(ScmConfig.getGIT_FLOW());

        // THEN
        SetReleaseVersionAction actual = underTest.create(workingDir);

        assertThat(actual.getConventionalCommitTypes()).isEqualTo(ConventionalCommitType.getDEFAULT_TYPES());
        assertThat(actual.getScmActions()).isEqualTo(scmActions);
        assertThat(actual.getScmConfig()).isEqualTo(ScmConfig.getGIT_FLOW());

        verifyNoInteractions(projectActions, scmActions, releaseVersionProvider, versionIncrementProvider);
        verify(projectActionsProvider).provide(workingDir);
        verify(releaseStepConfig).getConventionalCommitTypes();
        verify(releaseStepConfig).getScmActions();
        verify(releaseStepConfig).getScmConfig();
        verifyNoMoreInteractions(projectActionsProvider, releaseStepConfig);
    }

}
