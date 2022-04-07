package org.eazyportal.plugin.release.core.action

import org.eazyportal.plugin.release.core.model.ProjectDescriptor

interface ReleaseAction {

    fun execute(projectDescriptor: ProjectDescriptor)

}
