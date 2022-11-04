package org.eazyportal.plugin.release.core.scm.model

data class ScmConfig(
    val featureBranch: String,
    val releaseBranch: String,
    val remote: String
) {

    companion object {
        @JvmStatic
        val GIT_FLOW = ScmConfig("dev", "main", "origin")

        @JvmStatic
        val TRUNK_BASED_FLOW = ScmConfig("main", "main", "origin")
    }

}
