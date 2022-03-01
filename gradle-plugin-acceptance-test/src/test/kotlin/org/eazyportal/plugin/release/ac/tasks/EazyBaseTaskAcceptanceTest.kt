package org.eazyportal.plugin.release.ac.tasks

import org.eazyportal.plugin.release.ac.BasicAcceptanceTest
import org.eazyportal.plugin.release.ac.scm.StubScmActions
import org.junit.jupiter.api.BeforeEach

internal abstract class EazyBaseTaskAcceptanceTest : BasicAcceptanceTest() {

    @BeforeEach
    fun setUpBasicTaskAcceptanceTests() {
        BUILD_FILE.writeText("""
            plugins {
                `java`
                `maven-publish`
                id("org.eazyportal.plugin.release-gradle-plugin")
            }

            eazyRelease {
                scmActions = ${StubScmActions::class.java.name}()
            }
        """.trimIndent())
    }

}
