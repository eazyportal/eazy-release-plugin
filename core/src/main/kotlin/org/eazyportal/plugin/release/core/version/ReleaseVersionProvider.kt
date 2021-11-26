package org.eazyportal.plugin.release.core.version

import org.eazyportal.plugin.release.core.version.model.Version
import org.eazyportal.plugin.release.core.version.model.VersionIncrement

class ReleaseVersionProvider {

    /**
     * Returns with the release version.
     */
    fun provide(version: Version, versionIncrement: VersionIncrement): Version {
        return when (versionIncrement) {
            VersionIncrement.MAJOR -> Version(version.major + 1, 0, 0)
            VersionIncrement.MINOR -> Version(version.major, version.minor + 1, 0)
            VersionIncrement.PATCH -> {
                if (!version.preRelease.isNullOrBlank() || !version.build.isNullOrBlank()) {
                    Version(version.major, version.minor, version.patch)
                }
                else {
                    Version(version.major, version.minor, version.patch + 1)
                }
            }
        }
    }

}
