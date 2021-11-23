package org.eazyportal.plugin.release.gradle.tasks.exceptions

class InvalidVersionException(override val message: String) : RuntimeException(message)
