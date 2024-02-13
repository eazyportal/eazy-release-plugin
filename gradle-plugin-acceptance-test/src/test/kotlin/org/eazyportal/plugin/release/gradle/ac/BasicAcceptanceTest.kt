package org.eazyportal.plugin.release.gradle.ac

import org.eazyportal.plugin.release.gradle.project.GradleProjectActions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files

abstract class BasicAcceptanceTest {

    protected companion object {
        const val PROJECT_NAME = "dummy-project"

        @JvmStatic
        lateinit var BUILD_FILE: File
        @JvmStatic
        lateinit var GRADLE_PROPERTIES_FILE: File
        @JvmStatic
        lateinit var PROJECT_DIR: File
        @JvmStatic
        @TempDir
        lateinit var WORKING_DIR: File
    }

    @BeforeEach
    fun setUpBasicAcceptanceTests() {
        PROJECT_DIR = WORKING_DIR.resolve(PROJECT_NAME)
            .also { Files.createDirectories(it.toPath()) }

        BUILD_FILE = PROJECT_DIR.resolve("build.gradle.kts")

        GRADLE_PROPERTIES_FILE = PROJECT_DIR.resolve(GradleProjectActions.GRADLE_PROPERTIES_FILE_NAME)
    }

}
