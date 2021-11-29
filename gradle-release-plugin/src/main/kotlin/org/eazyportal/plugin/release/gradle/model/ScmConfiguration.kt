package org.eazyportal.plugin.release.gradle.model

import org.eazyportal.plugin.release.core.UpdateScmAction
import org.gradle.api.tasks.Input

open class ScmConfiguration {

    @get:Input
    var releaseBranch: String = UpdateScmAction.RELEASE_BRANCH

    @get:Input
    var remote: String = UpdateScmAction.REMOTE

}