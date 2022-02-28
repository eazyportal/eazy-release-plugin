package org.eazyportal.plugin.release.ac

import org.eazyportal.plugin.release.core.executor.CliCommandExecutor
import org.eazyportal.plugin.release.core.scm.GitActions
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal abstract class BaseEazyReleasePluginAcceptanceTest {

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
        fun initialize() {
            ORIGIN_PROJECT_DIR = WORKING_DIR.resolve("origin/$PROJECT_NAME")
                .also { Files.createDirectories(it.toPath()) }

            PROJECT_DIR = WORKING_DIR.resolve(PROJECT_NAME)
                .also { Files.createDirectories(it.toPath()) }
        }
    }

    internal fun File.copyIntoFromResources(fileName: String, subFolder: String = "") {
        val fileContent = "${this@BaseEazyReleasePluginAcceptanceTest::class.java.simpleName}/$subFolder/$fileName"
            .let {
                BaseEazyReleasePluginAcceptanceTest::class.java.classLoader.getResource(it)
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

    fun createGradleRunner(projectDir: File, vararg arguments: String): GradleRunner = GradleRunner.create()
        .forwardOutput()
        .withPluginClasspath()
        .withProjectDir(projectDir)
        .withArguments(*arguments)

}
