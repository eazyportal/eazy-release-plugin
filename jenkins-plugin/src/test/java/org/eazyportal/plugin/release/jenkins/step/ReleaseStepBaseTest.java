package org.eazyportal.plugin.release.jenkins.step;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.jenkins.action.ReleaseActionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;

import java.io.File;

public abstract class ReleaseStepBaseTest {

    @Mock
    protected EnvVars envVars;
    @Mock
    protected Launcher launcher;
    @Mock
    protected ReleaseActionFactory releaseActionFactory;
    @Mock
    protected Run<?, ?> run;
    @Mock
    protected TaskListener taskListener;

    protected FilePath workspace;

    @BeforeEach
    public void setUpBase(@TempDir File workingDir) {
        workspace = new FilePath(workingDir);
    }

}
