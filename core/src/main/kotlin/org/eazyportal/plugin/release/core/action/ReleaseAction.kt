package org.eazyportal.plugin.release.core.action

import java.io.File

interface ReleaseAction {

    fun execute(workingDir: File)

}
