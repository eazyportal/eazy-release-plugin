package org.eazyportal.plugin.release.gradle.ac.project

import org.assertj.core.api.Assertions.assertThat
import org.eazyportal.plugin.release.core.executor.CliCommandExecutor
import org.eazyportal.plugin.release.core.project.model.FileSystemProjectFile
import org.eazyportal.plugin.release.core.project.model.ProjectFile
import org.eazyportal.plugin.release.core.scm.GitActions
import org.eazyportal.plugin.release.gradle.project.GradleProjectActions
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseProjectAcceptanceTest {

    protected fun FileSystemProjectFile.copyIntoFromResources(fileName: String, subFolder: String = "") {
        val fileContent = "${this@BaseProjectAcceptanceTest::class.java.simpleName}/$subFolder/$fileName"
            .let {
                BaseProjectAcceptanceTest::class.java.classLoader.getResource(it)
                    ?: throw IllegalArgumentException("Resource is not found in classpath: $it")
            }
            .let { File(it.toURI()) }
            .readText()

        resolve(fileName)
            .let {
                Files.createDirectories(it.getFile().parentFile.toPath())

                it.writeText(fileContent)
            }
    }

    protected fun createGradleRunner(projectFile: FileSystemProjectFile, vararg arguments: String): GradleRunner =
        GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withProjectDir(projectFile.getFile())
            .withArguments(*arguments)

    protected fun FileSystemProjectFile.initializeGradleProject(subFolder: String = "") {
        createGradleRunner(this, "init", "--dsl", "kotlin")
            .build()

        copyIntoFromResources("build.gradle.kts", subFolder)
        copyIntoFromResources("settings.gradle.kts", subFolder)
        copyIntoFromResources(GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME, subFolder)
    }

    protected fun Pair<ProjectFile<File>, ProjectFile<File>>.verifyGitCommitsAndTags() =
        listOf(
            arrayOf("log", "--pretty=format:%s", "main"),
            arrayOf("log", "--pretty=format:%s", "dev"),
            arrayOf("tag")
        ).forEach {
            assertThat(SCM_ACTIONS.execute(first, *it)).isEqualTo(SCM_ACTIONS.execute(second, *it))
        }

    companion object {
        @JvmStatic
        protected val PROJECT_NAME = "dummy-project"

        @JvmStatic
        protected val CLI_EXECUTOR = CliCommandExecutor()
        @JvmStatic
        protected val SCM_ACTIONS = GitActions(CLI_EXECUTOR)

        @JvmStatic
        protected lateinit var ORIGIN_PROJECT_DIR: FileSystemProjectFile
        @JvmStatic
        protected lateinit var PROJECT_DIR: FileSystemProjectFile
        @JvmStatic
        @TempDir
        protected lateinit var WORKING_DIR: File

        @BeforeAll
        @JvmStatic
        fun initializeBaseProject() {
            ORIGIN_PROJECT_DIR = WORKING_DIR.resolve("origin/$PROJECT_NAME")
                .also { Files.createDirectories(it.toPath()) }
                .let { FileSystemProjectFile(it) }

            PROJECT_DIR = WORKING_DIR.resolve(PROJECT_NAME)
                .also { Files.createDirectories(it.toPath()) }
                .let { FileSystemProjectFile(it) }
        }
    }

}
