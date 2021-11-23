package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.version.model.Version
import org.eazyportal.plugin.release.gradle.tasks.exceptions.InvalidVersionException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetReleaseVersionTask @Inject constructor() : EazyBaseTask() {

    @get:Input
    lateinit var releaseVersion: Version

    @TaskAction
    fun run() {
        if (!releaseVersion.preRelease.isNullOrBlank() || !releaseVersion.build.isNullOrBlank()) {
            throw InvalidVersionException("Invalid release version: $releaseVersion")
        }

        logger.quiet("Setting version to $releaseVersion")
    }

}
