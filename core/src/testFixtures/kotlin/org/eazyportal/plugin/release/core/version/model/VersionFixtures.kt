package org.eazyportal.plugin.release.core.version.model

class VersionFixtures {

    companion object {
        @JvmStatic
        val RELEASE_001 = Version(0, 0, 1)
        @JvmStatic
        val RELEASE_002 = Version(0, 0, 2)
        @JvmStatic
        val RELEASE_003 = Version(0, 0, 3)

        @JvmStatic
        val RELEASE_010 = Version(0, 1, 0)
        @JvmStatic
        val RELEASE_020 = Version(0, 2, 0)

        @JvmStatic
        val RELEASE_100 = Version(1, 0, 0)
        @JvmStatic
        val RELEASE_200 = Version(2, 0, 0)

        @JvmStatic
        val SNAPSHOT_001 = Version(0, 0, 1, Version.DEVELOPMENT_VERSION_SUFFIX)
        @JvmStatic
        val SNAPSHOT_002 = Version(0, 0, 2, Version.DEVELOPMENT_VERSION_SUFFIX)

        @JvmStatic
        val SNAPSHOT_010 = Version(0, 1, 0, Version.DEVELOPMENT_VERSION_SUFFIX)
        @JvmStatic
        val SNAPSHOT_020 = Version(0, 2, 0, Version.DEVELOPMENT_VERSION_SUFFIX)

        @JvmStatic
        val SNAPSHOT_100 = Version(1, 0, 0, Version.DEVELOPMENT_VERSION_SUFFIX)
        @JvmStatic
        val SNAPSHOT_200 = Version(2, 0, 0, Version.DEVELOPMENT_VERSION_SUFFIX)
    }

}
