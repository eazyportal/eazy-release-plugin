package org.eazyportal.plugin.release.jenkins.action;

import hudson.EnvVars;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.TestFixtures;
import org.eazyportal.plugin.release.core.action.FinalizeReleaseVersionAction;
import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction;
import org.eazyportal.plugin.release.core.action.PrepareRepositoryForReleaseAction;
import org.eazyportal.plugin.release.core.action.ReleaseAction;
import org.eazyportal.plugin.release.core.action.SetReleaseVersionAction;
import org.eazyportal.plugin.release.core.action.SetSnapshotVersionAction;
import org.eazyportal.plugin.release.core.action.UpdateScmAction;
import org.eazyportal.plugin.release.core.model.Project;
import org.eazyportal.plugin.release.core.model.ProjectDescriptor;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.eazyportal.plugin.release.core.scm.model.ScmConfig;
import org.eazyportal.plugin.release.jenkins.ProjectDescriptorFactory;
import org.eazyportal.plugin.release.jenkins.ReleaseStepConfig;
import org.eazyportal.plugin.release.jenkins.scm.ScmActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReleaseActionFactoryTest {

    @Mock
    private ReleaseStepConfig releaseStepConfig;

    @InjectMocks
    private ReleaseActionFactory underTest;

    private static final ProjectDescriptor PROJECT_DESCRIPTOR = new ProjectDescriptor(
        new Project(mock(), mock()), Collections.emptyList(), Collections.emptyList()
    );

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @MethodSource("releaseActions")
    @ParameterizedTest
    public void test_create(Class<ReleaseAction> releaseActionClass) {
        Run<?, ?> run = mock();
        File workingDir = mock();
        EnvVars envVars = mock();
        Launcher launcher = mock();
        TaskListener taskListener = mock();

        ActionContextFactory actionContextFactory = mock();
        when(actionContextFactory.create(envVars)).thenReturn(TestFixtures.getACTION_CONTEXT());
        when(run.getAction(ActionContextFactory.class)).thenReturn(actionContextFactory);

        ScmActions scmActions = mock();
        ScmActionFactory scmActionFactory = mock();
        when(scmActionFactory.create(launcher, taskListener)).thenReturn(scmActions);
        when(run.getAction(ScmActionFactory.class)).thenReturn(scmActionFactory);

        ProjectDescriptorFactory projectDescriptorFactory = mock();
        when(projectDescriptorFactory.create(workingDir, scmActions)).thenReturn(PROJECT_DESCRIPTOR);
        when(run.getAction(ProjectDescriptorFactory.class)).thenReturn(projectDescriptorFactory);

        when(releaseStepConfig.getConventionalCommitTypes()).thenReturn(TestFixtures.getCONVENTIONAL_COMMIT_TYPES());
        when(releaseStepConfig.getScmConfig()).thenReturn(ScmConfig.getGIT_FLOW());

        assertThat(underTest.create(releaseActionClass, run, workingDir, envVars, launcher, taskListener))
            .isInstanceOf(releaseActionClass);
    }

    private static List<Arguments> releaseActions() {
        return List.of(
            Arguments.of(FinalizeReleaseVersionAction.class),
            Arguments.of(FinalizeSnapshotVersionAction.class),
            Arguments.of(PrepareRepositoryForReleaseAction.class),
            Arguments.of(SetReleaseVersionAction.class),
            Arguments.of(SetSnapshotVersionAction.class),
            Arguments.of(UpdateScmAction.class)
        );
    }

}
