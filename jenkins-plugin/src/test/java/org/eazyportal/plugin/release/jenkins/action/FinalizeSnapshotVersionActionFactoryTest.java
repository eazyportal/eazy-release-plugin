package org.eazyportal.plugin.release.jenkins.action;

import org.eazyportal.plugin.release.core.action.FinalizeSnapshotVersionAction;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class FinalizeSnapshotVersionActionFactoryTest {

    private FinalizeSnapshotVersionActionFactory underTest = new FinalizeSnapshotVersionActionFactory();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_create() {
        // GIVEN
        ScmActions scmActions = mock(ScmActions.class);

        // WHEN
        // THEN
        FinalizeSnapshotVersionAction actual = underTest.create(scmActions);

        assertThat(actual).hasNoNullFieldsOrProperties();

        verifyNoInteractions(scmActions);
    }

}
