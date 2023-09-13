package org.eazyportal.plugin.release.core.ac.scm

import org.eazyportal.plugin.release.core.scm.JGitActions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder

@Disabled(value = "JGit does not support Git submodules as standalone repositories.")
@TestMethodOrder(value = MethodOrderer.OrderAnnotation::class)
internal class JGitActionsMultiModuleAcceptanceTest : GitActionsMultiModuleAcceptanceTest() {

    @BeforeAll
    override fun initialize() {
        underTest = JGitActions()
    }

}
