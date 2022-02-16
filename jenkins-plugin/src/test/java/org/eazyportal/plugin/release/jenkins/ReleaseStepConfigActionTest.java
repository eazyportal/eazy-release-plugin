package org.eazyportal.plugin.release.jenkins;

import org.eazyportal.plugin.release.core.scm.ConventionalCommitType;
import org.eazyportal.plugin.release.core.scm.GitActions;
import org.eazyportal.plugin.release.core.scm.model.ScmConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class ReleaseStepConfigActionTest {

    @Mock
    private ReleaseStepConfigAction underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_instantiation() {
        // GIVEN
        // WHEN
        // THEN
        assertThat(underTest.getConventionalCommitTypes()).isEqualTo(ConventionalCommitType.getDEFAULT_TYPES());
        assertThat(underTest.getScmActions())
            .isInstanceOf(GitActions.class)
            .isNotNull();
        assertThat(underTest.getScmConfig()).isEqualTo(ScmConfig.getGIT_FLOW());
    }

}
