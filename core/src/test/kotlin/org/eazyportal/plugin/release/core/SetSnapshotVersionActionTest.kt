package org.eazyportal.plugin.release.core

import org.eazyportal.plugin.release.core.project.ProjectActions
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider
import org.eazyportal.plugin.release.core.version.model.Version
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.File

internal class SetSnapshotVersionActionTest {

    companion object {
        @JvmStatic
        private val RELEASE_001 = Version(0, 0, 1)
        @JvmStatic
        private val SNAPSHOT_002 = Version(0, 0, 2, Version.DEVELOPMENT_VERSION_SUFFIX)
    }

    private val workingDir = File("")

    @Mock
    private lateinit var projectActions: ProjectActions
    @Mock
    private lateinit var snapshotVersionProvider: SnapshotVersionProvider

    @InjectMocks
    private lateinit var underTest: SetSnapshotVersionAction

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_execute() {
        // GIVEN
        // WHEN
        whenever(projectActions.getVersion()).thenReturn(RELEASE_001)
        whenever(snapshotVersionProvider.provide(RELEASE_001)).thenReturn(SNAPSHOT_002)
        doNothing().whenever(projectActions).setVersion(SNAPSHOT_002)

        // THEN
        underTest.execute(workingDir)

        verify(projectActions).getVersion()
        verify(snapshotVersionProvider).provide(RELEASE_001)
        verify(projectActions).setVersion(SNAPSHOT_002)
        verifyNoMoreInteractions(projectActions, snapshotVersionProvider)
    }

}
