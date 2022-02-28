package org.eazyportal.plugin.release.ac

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.gradle.project.GradleProjectActions
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(value = OrderAnnotation::class)
internal class GitFlowProjectAcceptanceTest : BaseEazyReleasePluginAcceptanceTest() {

    @Order(0)
    @Test
    fun test_initializeRepository() {
        // GIVEN
        SCM_ACTIONS.execute(ORIGIN_PROJECT_DIR, "init")

        createGradleRunner(ORIGIN_PROJECT_DIR, "init", "--dsl", "kotlin")
            .build()

        ORIGIN_PROJECT_DIR.copyIntoFromResources("build.gradle.kts")
        ORIGIN_PROJECT_DIR.copyIntoFromResources("settings.gradle.kts")
        ORIGIN_PROJECT_DIR.copyIntoFromResources(GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME)

        // WHEN
        SCM_ACTIONS.add(ORIGIN_PROJECT_DIR, ".")
        SCM_ACTIONS.commit(ORIGIN_PROJECT_DIR, "initial commit")

        // THEN
        assertThat(SCM_ACTIONS.getCommits(ORIGIN_PROJECT_DIR)).containsExactly("initial commit")

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
        assertThat(buildResult.output.lines()).containsSubsequence(
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
        assertThat(buildResult.output.lines()).containsSubsequence(
            "> Task :setReleaseVersion",
            "Setting release version...",
            "1 actionable task: 1 executed"
        )

        SCM_ACTIONS.execute(PROJECT_DIR, "status")
            .run {
                assertThat(lines()).containsSubsequence(
                    "On branch master",
                    "nothing to commit, working tree clean"
                )
            }
        assertThat(SCM_ACTIONS.getCommits(PROJECT_DIR).first()).isEqualTo("Release version: 0.1.0")
        assertThat(SCM_ACTIONS.getLastTag(PROJECT_DIR)).isEqualTo("0.1.0")

        assertThat(GradleProjectActions(PROJECT_DIR).getVersion()).hasToString("0.1.0")
    }

    @Order(11)
    @Test
    fun test_releaseBuild() {
        // GIVEN
        // WHEN
        val buildResult = createGradleRunner(PROJECT_DIR, "releaseBuild")
            .build()

        // THEN
        assertThat(buildResult.output.lines()).containsSubsequence(
            "> Task :jar",
            "> Task :build",
            "> Task :publish",
            "> Task :releaseBuild",
            "5 actionable tasks: 5 executed"
        )

        PROJECT_DIR.resolve("build/libs/")
            .run {
                assertThat(resolve("$PROJECT_NAME-0.1.0.jar").exists()).isTrue
                assertThat(resolve("$PROJECT_NAME-0.0.1-SNAPSHOT.jar").exists()).isFalse
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
        assertThat(buildResult.output.lines()).containsSubsequence(
            "> Task :setSnapshotVersion",
            "Setting SNAPSHOT version...",
            "1 actionable task: 1 executed"
        )

        SCM_ACTIONS.execute(PROJECT_DIR, "status")
            .run {
                assertThat(lines()).containsSubsequence(
                    "On branch feature",
                    "nothing to commit, working tree clean"
                )
            }
        assertThat(SCM_ACTIONS.getCommits(PROJECT_DIR).first()).isEqualTo("New snapshot version: 0.1.1-SNAPSHOT")
        assertThat(SCM_ACTIONS.getLastTag(PROJECT_DIR)).isEqualTo("0.1.0")

        assertThat(GradleProjectActions(PROJECT_DIR).getVersion()).hasToString("0.1.1-SNAPSHOT")
    }

    @Order(13)
    @Test
    fun test_updateScm() {
        // GIVEN
        // WHEN
        val buildResult = createGradleRunner(PROJECT_DIR, "updateScm")
            .build()

        // THEN
        assertThat(buildResult.output.lines()).containsSubsequence(
            "> Task :updateScm",
            "1 actionable task: 1 executed"
        )

        listOf(
            arrayOf("log", "--pretty=format:%s", "master"),
            arrayOf("log", "--pretty=format:%s", "feature"),
            arrayOf("tag")
        ).forEach {
            assertThat(SCM_ACTIONS.execute(PROJECT_DIR, *it)).isEqualTo(SCM_ACTIONS.execute(ORIGIN_PROJECT_DIR, *it))
        }
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
        assertThat(buildResult.output.lines()).containsSubsequence(
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
    }

}
