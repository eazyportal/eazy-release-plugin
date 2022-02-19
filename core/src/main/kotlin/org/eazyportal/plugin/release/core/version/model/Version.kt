package org.eazyportal.plugin.release.core.version.model

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    var preRelease: String? = null,
    var build: String? = null
) {

    companion object {
        const val DEVELOPMENT_VERSION_SUFFIX = "SNAPSHOT"

        private val versionRegex = Regex("^(\\d+)\\.(\\d+)\\.(\\d+)-?([a-zA-Z-\\d\\.]*)\\+?([a-zA-Z-\\d\\.]*)$")

        fun of(versionValue: String): Version {
            val versionMatch = versionRegex.find(versionValue)
                ?: throw IllegalArgumentException("Failed to parse provided version: $versionValue")

            val (major, minor, patch, preRelease, build) = versionMatch.destructured

            return Version(major.toInt(), minor.toInt(), patch.toInt(), preRelease, build)
        }
    }

    init {
        if ((major < 0) || (minor < 0) || (patch < 0)) {
            throw IllegalArgumentException("Version cannot have negative major, minor, or patch values.")
        }

        if (preRelease.isNullOrBlank()) {
            preRelease = null
        }
        else if (preRelease!!.startsWith("0")) {
            // https://semver.org/#spec-item-9
            throw IllegalArgumentException("Pre-release should not start with '0'.")
        }

        if (build.isNullOrBlank()) {
            build = null
        }
    }

    fun isRelease(): Boolean {
        return preRelease.isNullOrBlank() && build.isNullOrBlank()
    }

    override fun toString(): String {
        val sb = StringBuilder(9)

        sb.append(major)
        sb.append(".")
        sb.append(minor)
        sb.append(".")
        sb.append(patch)

        preRelease?.let {
            sb.append("-")
            sb.append(it)
        }

        build?.let {
            sb.append("+")
            sb.append(it)
        }

        return sb.toString()
    }

    operator fun compareTo(other: Version?): Int =
        VersionComparator().compare(this, other)

}
