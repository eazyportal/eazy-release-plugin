package org.eazyportal.plugin.release.core.action

import org.junit.jupiter.api.io.TempDir
import java.io.File

abstract class ReleaseActionBaseTest {

    protected companion object {
        const val FILE_TO_COMMIT = "."
    }

    @TempDir
    protected lateinit var workingDir: File

}
