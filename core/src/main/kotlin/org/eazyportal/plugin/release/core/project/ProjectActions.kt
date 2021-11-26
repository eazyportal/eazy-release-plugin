package org.eazyportal.plugin.release.core.project

import org.eazyportal.plugin.release.core.version.model.Version

interface ProjectActions {

    fun getVersion(): Version

    fun setVersion(version: Version)

}
