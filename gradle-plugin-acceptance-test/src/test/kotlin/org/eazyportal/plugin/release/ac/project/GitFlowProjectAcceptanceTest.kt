package org.eazyportal.plugin.release.ac.project

import org.assertj.core.api.Assertions
import org.eazyportal.plugin.release.gradle.project.GradleProjectActions
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(value = OrderAnnotation::class)
internal class GitFlowProjectAcceptanceTest : BaseProjectAcceptanceTest() {

    @Order(0)
    @Test
    fun test_initializeRepository() {
        // GIVEN
        ORIGIN_PROJECT_DIR.run {
            SCM_ACTIONS.execute(this, "init")

            initializeGradleProject()
        }

        // WHEN
        SCM_ACTIONS.add(ORIGIN_PROJECT_DIR, ".")
        SCM_ACTIONS.commit(ORIGIN_PROJECT_DIR, "initial commit")

        // THEN
        Assertions.assertThat(SCM_ACTIONS.getCommits(ORIGIN_PROJECT_DIR)).containsExactly("initial commit")

        SCM_ACTIONS.execute(ORIGIN_PROJECT_DIR, "checkout", "-b", "feature")

        SCM_ACTIONS.execute(WORKING_DIR, "clone", ORIGIN_PROJECT_DIR.resolve(".git").path, PROJECT_NAME)

        // Checking out to a different branch will fix: "remote: error: refusing to update checked out branch: refs/heads/feature"
        SCM_ACTIONS.execute(ORIGIN_PROJECT_DIR, "checkout", "-b", "tmp")
    }

    @Order(1)
    @Test
    fun test_release_shouldFail_whenThereAreNoAcceptableCommits() {
        // GIVEN
        // WHEN
        val buildResult = createGradleRunner(PROJECT_DIR, "release")
            .buildAndFail()

        // THEN
        Assertions.assertThat(buildResult.output.lines()).containsSubsequence(
            "Setting release version...",
            "Ignoring missing Git tag from release version calculation.",
            "Ignoring invalid commit: initial commit",
            "FAILURE: Build failed with an exception.",
            "* What went wrong:",
            "Execution failed for task ':setReleaseVersion'.",
            "> There are no acceptable commits."
        )
    }

    @Order(10)
    @Test
    fun test_setReleaseVersion() {
        // GIVEN
        PROJECT_DIR.copyIntoFromResources("src/main/java/org/eazyportal/plugin/release/test/dummy/DummyApplication.java")

        // WHEN
        SCM_ACTIONS.add(PROJECT_DIR, ".")
        SCM_ACTIONS.commit(PROJECT_DIR, "feature: implement feature")

        val buildResult = createGradleRunner(PROJECT_DIR, "setReleaseVersion")
            .build()

        // THEN
        Assertions.assertThat(buildResult.output.lines()).containsSubsequence(
            "> Task :setReleaseVersion",
            "Setting release version...",
            "1 actionable task: 1 executed"
        )

        SCM_ACTIONS.execute(PROJECT_DIR, "status")
            .run {
                Assertions.assertThat(lines()).containsSubsequence(
                    "On branch master",
                    "nothing to commit, working tree clean"
                )
            }
        Assertions.assertThat(SCM_ACTIONS.getCommits(PROJECT_DIR).first()).isEqualTo("Release version: 0.1.0")
        Assertions.assertThat(SCM_ACTIONS.getLastTag(PROJECT_DIR)).isEqualTo("0.1.0")

        Assertions.assertThat(GradleProjectActions(PROJECT_DIR).getVersion()).hasToString("0.1.0")
    }

    @Order(11)
    @Test
    fun test_releaseBuild() {
        // GIVEN
        // WHEN
        val buildResult = createGradleRunner(PROJECT_DIR, "releaseBuild")
            .build()

        // THEN
        Assertions.assertThat(buildResult.output.lines()).containsSubsequence(
            "> Task :jar",
            "> Task :build",
            "> Task :publish",
            "> Task :releaseBuild",
            "5 actionable tasks: 5 executed"
        )

        PROJECT_DIR.resolve("build/libs/")
            .run {
                Assertions.assertThat(resolve("$PROJECT_NAME-0.1.0.jar").exists()).isTrue
                Assertions.assertThat(resolve("$PROJECT_NAME-0.0.1-SNAPSHOT.jar").exists()).isFalse
            }
    }

    @Order(12)
    @Test
    fun test_setSnapshotVersion() {
        // GIVEN
        // WHEN
        val buildResult = createGradleRunner(PROJECT_DIR, "setSnapshotVersion")
            .build()

        // THEN
        Assertions.assertThat(buildResult.output.lines()).containsSubsequence(
            "> Task :setSnapshotVersion",
            "Setting SNAPSHOT version...",
            "1 actionable task: 1 executed"
        )

        SCM_ACTIONS.execute(PROJECT_DIR, "status")
            .run {
                Assertions.assertThat(lines()).containsSubsequence(
                    "On branch feature",
                    "nothing to commit, working tree clean"
                )
            }
        Assertions.assertThat(SCM_ACTIONS.getCommits(PROJECT_DIR).first()).isEqualTo("New snapshot version: 0.1.1-SNAPSHOT")
        Assertions.assertThat(SCM_ACTIONS.getLastTag(PROJECT_DIR)).isEqualTo("0.1.0")

        Assertions.assertThat(GradleProjectActions(PROJECT_DIR).getVersion()).hasToString("0.1.1-SNAPSHOT")
    }

    @Order(13)
    @Test
    fun test_updateScm() {
        // GIVEN
        // WHEN
        val buildResult = createGradleRunner(PROJECT_DIR, "updateScm")
            .build()

        // THEN
        Assertions.assertThat(buildResult.output.lines()).containsSubsequence(
            "> Task :updateScm",
            "1 actionable task: 1 executed"
        )

        listOf(PROJECT_DIR to ORIGIN_PROJECT_DIR)
            .forEach { it.verifyGitCommitsAndTags() }
    }

    @Order(20)
    @Test
    fun test_release() {
        // GIVEN
        PROJECT_DIR.copyIntoFromResources("src/main/java/org/eazyportal/plugin/release/test/dummy/service/DummyService.java")

        // WHEN
        SCM_ACTIONS.add(PROJECT_DIR, ".")
        SCM_ACTIONS.commit(PROJECT_DIR, "feature: implement service")

        val buildResult = createGradleRunner(PROJECT_DIR, "release")
            .build()

        // THEN
        Assertions.assertThat(buildResult.output.lines()).containsSubsequence(
            "> Task :setReleaseVersion",
            "Setting release version...",
            "> Task :jar",
            "> Task :build",
            "> Task :publish",
            "> Task :releaseBuild",
            "> Task :setSnapshotVersion",
            "Setting SNAPSHOT version...",
            "> Task :updateScm",
            "Updating scm...",
            "> Task :release",
            "8 actionable tasks: 8 executed"
        )

        listOf(PROJECT_DIR to ORIGIN_PROJECT_DIR)
            .forEach { it.verifyGitCommitsAndTags() }
    }

}