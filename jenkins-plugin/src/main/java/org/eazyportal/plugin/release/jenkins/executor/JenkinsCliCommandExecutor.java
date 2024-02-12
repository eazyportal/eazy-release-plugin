package org.eazyportal.plugin.release.jenkins.executor;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.TaskListener;
import org.eazyportal.plugin.release.core.executor.CommandExecutor;
import org.eazyportal.plugin.release.core.executor.exception.CliExecutionException;
import org.eazyportal.plugin.release.core.project.model.ProjectFile;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class JenkinsCliCommandExecutor implements CommandExecutor<ProjectFile<FilePath>> {

    private final Launcher launcher;
    private final TaskListener listener;

    public JenkinsCliCommandExecutor(Launcher launcher, TaskListener listener) {
        this.launcher = Objects.requireNonNull(launcher);
        this.listener = Objects.requireNonNull(listener);
    }

    @NotNull
    @Override
    public String execute(@NotNull ProjectFile<FilePath> projectFile, @NotNull String... commands) throws CliExecutionException {
        try (
            var stdOutOutputStream = new ByteArrayOutputStream();
            var stdErrOutputStream = new ByteArrayOutputStream()
        ) {
            var returnCode = launcher.launch()
                .cmds(commands)
                .stdout(stdOutOutputStream)
                .stderr(stdErrOutputStream)
                .pwd(projectFile.getFile())
                .join();

            if (returnCode == 0) {
                String result = stdOutOutputStream.toString();

                listener.getLogger().println(result);

                return result;
            } else {
                String result = stdErrOutputStream.toString();
                if (result.isBlank()) {
                    result = stdOutOutputStream.toString();
                }

                listener.getLogger().println(result);

                throw new CliExecutionException(result);
            }
        } catch (IOException | InterruptedException exception) {
            listener.getLogger().println(exception.getMessage());

            throw new CliExecutionException(exception.getMessage());
        }
    }

}
