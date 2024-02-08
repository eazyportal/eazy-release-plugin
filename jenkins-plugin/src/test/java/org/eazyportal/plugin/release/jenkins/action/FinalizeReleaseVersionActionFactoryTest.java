package org.eazyportal.plugin.release.jenkins.action;

import org.eazyportal.plugin.release.core.action.FinalizeReleaseVersionAction;
import org.eazyportal.plugin.release.core.scm.ScmActions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class FinalizeReleaseVersionActionFactoryTest {

    private FinalizeReleaseVersionActionFactory underTest = new FinalizeReleaseVersionActionFactory();

    @Test
    void test_create() {
        // GIVEN
        ScmActions scmActions = mock(ScmActions.class);

        // WHEN
        // THEN
        FinalizeReleaseVersionAction actual = underTest.create(scmActions);

        assertThat(actual).hasNoNullFieldsOrProperties();

        verifyNoInteractions(scmActions);
    }

}
