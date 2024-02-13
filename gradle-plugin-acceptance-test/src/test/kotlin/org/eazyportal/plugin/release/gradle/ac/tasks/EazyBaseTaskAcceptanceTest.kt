package org.eazyportal.plugin.release.gradle.ac.tasks

import org.eazyportal.plugin.release.gradle.ac.BasicAcceptanceTest
import org.eazyportal.plugin.release.gradle.ac.scm.StubScmActions
import org.junit.jupiter.api.BeforeEach

internal abstract class EazyBaseTaskAcceptanceTest : BasicAcceptanceTest() {

    @BeforeEach
    fun setUpBasicTaskAcceptanceTests() {
        BUILD_FILE.writeText("""
            plugins {
                `java`
                `maven-publish`
                id("org.eazyportal.plugin.release")
            }

            eazyRelease {
                scmActions = ${StubScmActions::class.java.name}()
            }
        """.trimIndent())
    }

}
