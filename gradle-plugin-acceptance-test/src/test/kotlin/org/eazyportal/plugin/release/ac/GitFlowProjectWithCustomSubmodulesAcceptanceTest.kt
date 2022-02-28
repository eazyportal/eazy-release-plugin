package org.eazyportal.plugin.release.ac

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.ac.project.StubProjectActionsFactory
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.io.File
import java.nio.file.Files

@TestMethodOrder(value = OrderAnnotation::class)
internal class GitFlowProjectWithCustomSubmodulesAcceptanceTest : BaseEazyReleasePluginAcceptanceTest() {

    private companion object {
        const val SUBMODULE_PROJECT_NAME = "submodule-project"

        @JvmStatic
        val PROJECT_ACTIONS_FACTORY = StubProjectActionsFactory()

        @JvmStatic
        lateinit var ALL_PROJECT_DIRS: List<File>
        @JvmStatic
        lateinit var ORIGIN_SUBMODULE_PROJECT_DIR: File
        @JvmStatic
        lateinit var SUBMODULE_PROJECT_DIR: File

        @BeforeAll
        @JvmStatic
        fun initialize() {
            BaseEazyReleasePluginAcceptanceTest.initialize()

            ORIGIN_SUBMODULE_PROJECT_DIR = WORKING_DIR.resolve("origin/$SUBMODULE_PROJECT_NAME")
                .also { Files.createDirectories(it.toPath()) }
                .also { SCM_ACTIONS.execute(it, "init") }

            SUBMODULE_PROJECT_DIR = PROJECT_DIR.resolve(SUBMODULE_PROJECT_NAME)

            ALL_PROJECT_DIRS = listOf(PROJECT_DIR, SUBMODULE_PROJECT_DIR)
        }
    }

    @Order(0)
    @Test
    fun test_initializeRepository() {
        // GIVEN
        ORIGIN_PROJECT_DIR.run {
            SCM_ACTIONS.execute(this, "init")

            initializeGradleProject(PROJECT_NAME)
        }

        ORIGIN_SUBMODULE_PROJECT_DIR.run {
            SCM_ACTIONS.execute(this, "init")

            copyIntoFromResources("version.json", SUBMODULE_PROJECT_NAME)
        }

        listOf(ORIGIN_PROJECT_DIR, ORIGIN_SUBMODULE_PROJECT_DIR)
            .forEach { projectDir ->
                // WHEN
                SCM_ACTIONS.add(projectDir, ".")
                SCM_ACTIONS.commit(projectDir, "initial commit")

                // THEN
                assertThat(SCM_ACTIONS.getCommits(projectDir)).containsExactly("initial commit")

                SCM_ACTIONS.execute(projectDir, "checkout", "-b", "feature")
            }

        val originProjectGitDirPath = ORIGIN_PROJECT_DIR.resolve(".git").path
        val originSubmoduleGitDirPath = ORIGIN_SUBMODULE_PROJECT_DIR.resolve(".git").path

        // Fixing Windows folder separator issue
        SCM_ACTIONS.execute(ORIGIN_PROJECT_DIR, "submodule", "add", originSubmoduleGitDirPath.replace("\\", "/"))

        SCM_ACTIONS.commit(ORIGIN_PROJECT_DIR, "add submodule")

        SCM_ACTIONS.execute(WORKING_DIR, "clone", "--recurse-submodules", originProjectGitDirPath, PROJECT_NAME)

        // Checking out to a different branch will fix: "remote: error: refusing to update checked out branch: refs/heads/feature"
        listOf(ORIGIN_PROJECT_DIR, ORIGIN_SUBMODULE_PROJECT_DIR).forEach { projectDir ->
            SCM_ACTIONS.execute(projectDir, "checkout", "-b", "tmp")
        }
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
        PROJECT_DIR.copyIntoFromResources("src/main/java/org/eazyportal/plugin/release/test/dummy/DummyApplication.java", PROJECT_NAME)

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

        assertThat(SCM_ACTIONS.getLastTag(PROJECT_DIR)).isEqualTo("0.1.0")
        ALL_PROJECT_DIRS.forEach { projectDir ->
            SCM_ACTIONS.execute(projectDir, "status")
                .run {
                    assertThat(lines()).containsSubsequence(
                        "On branch master",
                        "nothing to commit, working tree clean"
                    )
                }
            assertThat(SCM_ACTIONS.getCommits(projectDir).first()).isEqualTo("Release version: 0.1.0")

            assertThat(PROJECT_ACTIONS_FACTORY.create(projectDir).getVersion()).hasToString("0.1.0")
        }
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
            "> Task :submodule-project:build",
            "Hello from custom build task!",
            "> Task :submodule-project:publish",
            "Hello from custom publish task!",
            "> Task :releaseBuild",
            "7 actionable tasks: 7 executed"
        )

        PROJECT_DIR.resolve("build/libs/")
            .run {
                assertThat(resolve("$PROJECT_NAME-0.1.0.jar").exists()).isTrue
                assertThat(resolve("$PROJECT_NAME-0.0.1-SNAPSHOT.jar").exists()).isFalse
            }

        SUBMODULE_PROJECT_DIR.resolve("build/libs/")
            .run {
                assertThat(resolve("$SUBMODULE_PROJECT_NAME-0.1.0.jar").exists()).isFalse
                assertThat(resolve("$SUBMODULE_PROJECT_NAME-0.0.1-SNAPSHOT.jar").exists()).isFalse
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

        assertThat(SCM_ACTIONS.getLastTag(PROJECT_DIR)).isEqualTo("0.1.0")
        ALL_PROJECT_DIRS.forEach { projectDir ->
            SCM_ACTIONS.execute(projectDir, "status")
                .run {
                    assertThat(lines()).containsSubsequence(
                        "On branch feature",
                        "nothing to commit, working tree clean"
                    )
                }
            assertThat(SCM_ACTIONS.getCommits(projectDir).first()).isEqualTo("New snapshot version: 0.1.1-SNAPSHOT")

            PROJECT_ACTIONS_FACTORY.create(projectDir)
                .getVersion()
                .run { assertThat(this).hasToString("0.1.1-SNAPSHOT") }
        }
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

        listOf(PROJECT_DIR to ORIGIN_PROJECT_DIR, SUBMODULE_PROJECT_DIR to ORIGIN_SUBMODULE_PROJECT_DIR)
            .forEach { it.verifyGitCommitsAndTags() }
    }

    @Order(20)
    @Test
    fun test_release() {
        // GIVEN
        SUBMODULE_PROJECT_DIR.copyIntoFromResources("README.adoc", SUBMODULE_PROJECT_NAME)

        // WHEN
        SCM_ACTIONS.add(SUBMODULE_PROJECT_DIR, ".")
        SCM_ACTIONS.commit(SUBMODULE_PROJECT_DIR, "feature: implement service")

        val buildResult = createGradleRunner(PROJECT_DIR, "release")
            .build()

        // THEN
        assertThat(buildResult.output.lines()).containsSubsequence(
            "> Task :setReleaseVersion",
            "Setting release version...",
            "> Task :jar",
            "> Task :build",
            "> Task :publish",
            "> Task :submodule-project:build",
            "Hello from custom build task!",
            "> Task :submodule-project:publish",
            "Hello from custom publish task!",
            "> Task :releaseBuild",
            "> Task :setSnapshotVersion",
            "Setting SNAPSHOT version...",
            "> Task :updateScm",
            "Updating scm...",
            "> Task :release",
            "10 actionable tasks: 9 executed, 1 up-to-date"
        )

        listOf(PROJECT_DIR to ORIGIN_PROJECT_DIR, SUBMODULE_PROJECT_DIR to ORIGIN_SUBMODULE_PROJECT_DIR)
            .forEach { it.verifyGitCommitsAndTags() }
    }

}
