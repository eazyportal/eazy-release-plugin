package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.action.model.ActionContext
import org.eazyportal.plugin.release.core.scm.ConventionalCommitType

object TestFixtures {

    @JvmStatic
    val ACTION_CONTEXT = ActionContext(
        isForceRelease = false
    )

    @JvmStatic
    val CONVENTIONAL_COMMIT_TYPES = ConventionalCommitType.DEFAULT_TYPES

}
