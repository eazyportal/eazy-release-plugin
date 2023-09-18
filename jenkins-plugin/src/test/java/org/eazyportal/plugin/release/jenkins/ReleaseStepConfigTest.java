package org.eazyportal.plugin.release.jenkins;

import org.eazyportal.plugin.release.core.scm.ConventionalCommitType;
import org.eazyportal.plugin.release.core.scm.JGitActions;
import org.eazyportal.plugin.release.core.scm.model.ScmConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReleaseStepConfigTest {

    private ReleaseStepConfig underTest;

    @BeforeEach
    void setUp() {
        underTest = new ReleaseStepConfig();
    }

    @Test
    void test_instantiation() {
        // GIVEN
        // WHEN
        // THEN
        assertThat(underTest.getConventionalCommitTypes()).isEqualTo(ConventionalCommitType.getDEFAULT_TYPES());
        assertThat(underTest.getScmActions())
                .isInstanceOf(JGitActions.class)
                .isNotNull();
        assertThat(underTest.getScmConfig()).isEqualTo(ScmConfig.getGIT_FLOW());
    }

}
