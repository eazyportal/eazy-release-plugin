package org.eazyportal.plugin.release.core.action

import org.junit.jupiter.api.io.TempDir
import java.io.File

abstract class ReleaseActionBaseTest {

    @TempDir
    protected lateinit var workingDir: File

    companion object {
        @JvmStatic
        protected val FILE_TO_COMMIT = "."
    }

}
