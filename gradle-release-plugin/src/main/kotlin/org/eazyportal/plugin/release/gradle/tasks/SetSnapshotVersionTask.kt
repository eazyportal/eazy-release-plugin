package org.eazyportal.plugin.release.gradle.tasks

import org.eazyportal.plugin.release.core.version.model.Version
import org.eazyportal.plugin.release.gradle.tasks.exceptions.InvalidVersionException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetSnapshotVersionTask @Inject constructor() : EazyBaseTask() {

    @get:Input
    lateinit var snapshotVersion: Version

    @TaskAction
    fun run() {
        if (snapshotVersion.preRelease.isNullOrBlank() && snapshotVersion.build.isNullOrBlank()) {
            throw InvalidVersionException("Invalid snapshot version: $snapshotVersion")
        }

        logger.quiet("Setting version to $snapshotVersion")
    }

}
