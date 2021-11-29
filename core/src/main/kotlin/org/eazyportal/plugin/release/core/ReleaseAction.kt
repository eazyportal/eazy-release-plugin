package org.eazyportal.plugin.release.core

import java.io.File

interface ReleaseAction {

    fun execute(workingDir: File)

}
