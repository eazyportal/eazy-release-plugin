package org.eazyportal.plugin.release.gradle.ac.project

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.core.executor.CliCommandExecutor
import org.eazyportal.plugin.release.core.scm.GitActions
import org.eazyportal.plugin.release.gradle.project.GradleProjectActions
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal abstract class BaseProjectAcceptanceTest {

    companion object {
        const val PROJECT_NAME = "dummy-project"

        @JvmStatic
        val CLI_EXECUTOR = CliCommandExecutor()
        @JvmStatic
        val SCM_ACTIONS = GitActions(CLI_EXECUTOR)

        @JvmStatic
        lateinit var ORIGIN_PROJECT_DIR: File
        @JvmStatic
        lateinit var PROJECT_DIR: File
        @TempDir
        @JvmStatic
        lateinit var WORKING_DIR: File

        @BeforeAll
        @JvmStatic
        fun initializeBaseProject() {
            ORIGIN_PROJECT_DIR = WORKING_DIR.resolve("origin/$PROJECT_NAME")
                .also { Files.createDirectories(it.toPath()) }

            PROJECT_DIR = WORKING_DIR.resolve(PROJECT_NAME)
                .also { Files.createDirectories(it.toPath()) }
        }
    }

    internal fun File.copyIntoFromResources(fileName: String, subFolder: String = "") {
        val fileContent = "${this@BaseProjectAcceptanceTest::class.java.simpleName}/$subFolder/$fileName"
            .let {
                BaseProjectAcceptanceTest::class.java.classLoader.getResource(it)
                    ?: throw IllegalArgumentException("Resource is not found in classpath: $it")
            }
            .let { File(it.toURI()) }
            .readText()

        resolve(fileName)
            .let {
                Files.createDirectories(it.parentFile.toPath())

                it.writeText(fileContent)
            }
    }

    internal fun createGradleRunner(projectDir: File, vararg arguments: String): GradleRunner =
        GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withProjectDir(projectDir)
            .withArguments(*arguments)

    internal fun File.initializeGradleProject(subFolder: String = "") {
        createGradleRunner(this, "init", "--dsl", "kotlin")
            .build()

        copyIntoFromResources("build.gradle.kts", subFolder)
        copyIntoFromResources("settings.gradle.kts", subFolder)
        copyIntoFromResources(GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME, subFolder)
    }

    internal fun Pair<File, File>.verifyGitCommitsAndTags() =
        listOf(
            arrayOf("log", "--pretty=format:%s", "main"),
            arrayOf("log", "--pretty=format:%s", "dev"),
            arrayOf("tag")
        ).forEach {
            assertThat(SCM_ACTIONS.execute(first, *it)).isEqualTo(SCM_ACTIONS.execute(second, *it))
        }

}
