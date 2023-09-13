package org.eazyportal.plugin.release.core.ac.scm

import org.eazyportal.plugin.release.core.scm.JGitActions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(value = MethodOrderer.OrderAnnotation::class)
internal class JGitActionsAcceptanceTest : GitActionsAcceptanceTest() {

    @BeforeAll
    override fun initialize() {
        underTest = JGitActions()
    }

}
