package org.eazyportal.plugin.release.core.version

import org.eazyportal.plugin.release.core.version.exception.InvalidVersionException
import org.eazyportal.plugin.release.core.version.model.Version

class SnapshotVersionProvider {

    /**
     * Returns with the next SNAPSHOT version.
     */
    fun provide(version: Version): Version {
        if (!version.isRelease()) {
            throw InvalidVersionException("Failed to set SNAPSHOT version, because project already on SNAPSHOT version.")
        }

        return Version(version.major, version.minor, version.patch + 1, Version.DEVELOPMENT_VERSION_SUFFIX)
    }

}
