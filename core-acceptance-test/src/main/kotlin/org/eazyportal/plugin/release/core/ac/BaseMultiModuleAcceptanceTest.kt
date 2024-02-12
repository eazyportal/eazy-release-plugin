package org.eazyportal.plugin.release.core.ac

import org.eazyportal.plugin.release.core.project.model.FileSystemProjectFile
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.nio.file.Files

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseMultiModuleAcceptanceTest : BaseAcceptanceTest() {

    protected lateinit var allOriginProject: Map<String, FileSystemProjectFile>
    protected lateinit var originSubProjectDir: FileSystemProjectFile

    protected lateinit var allProject: Map<String, FileSystemProjectFile>
    protected lateinit var subProjectDir: FileSystemProjectFile

    @BeforeAll
    fun initializeMultiModuleBase() {
        originSubProjectDir = workingDir.resolve("origin/$SUB_PROJECT_NAME")
            .also { Files.createDirectories(it.toPath()) }
            .let { FileSystemProjectFile(it) }

        subProjectDir = projectDir.resolve(SUB_PROJECT_NAME)

        allOriginProject = mapOf(
            SUB_PROJECT_NAME to originSubProjectDir,
            PROJECT_NAME to originProjectDir
        )

        allProject = mapOf(
            SUB_PROJECT_NAME to subProjectDir,
            PROJECT_NAME to projectDir
        )
    }

    protected companion object {
        const val SUB_PROJECT_NAME = "dummy-sub-project"
    }

}