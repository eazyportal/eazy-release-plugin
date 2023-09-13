package org.eazyportal.plugin.release.core.ac

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseAcceptanceTest {

    protected lateinit var originProjectDir: File

    protected lateinit var projectDir: File

    protected lateinit var workingDir: File

    @BeforeAll
    fun initializeBase(@TempDir tempDir: File) {
        workingDir = tempDir

        originProjectDir = workingDir.resolve("origin/$PROJECT_NAME")
            .also { Files.createDirectories(it.toPath()) }

        projectDir = workingDir.resolve(PROJECT_NAME)
    }

    protected companion object {
        const val BRANCH_FEATURE = "dev"
        const val BRANCH_MAIN = "main"
        const val REMOTE = "origin"

        const val PROJECT_NAME = "dummy-project"

        fun File.copyResource(fileName: String, resourceSubFolder: String? = null): File {
            val resourceName = if (resourceSubFolder.isNullOrBlank()) {
                fileName
            } else {
                "$resourceSubFolder/$fileName"
            }

            val resourceFile = BaseAcceptanceTest::class.java.classLoader.getResource(resourceName)
                ?.let { File(it.toURI()) }
                ?: throw IllegalArgumentException("Resource is not found in classpath: $fileName")

            return resolve(fileName).apply {
                Files.createDirectories(parentFile.toPath())

                if (resourceFile.isDirectory) {
                    resourceFile.copyRecursively(this, true)
                } else {
                    writeText(resourceFile.readText())
                }
            }
        }
    }

}