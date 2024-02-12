package org.eazyportal.plugin.release.jenkins.executor;

import hudson.Launcher;
import hudson.model.TaskListener;
import io.jenkins.cli.shaded.org.apache.commons.lang.SystemUtils;
import org.eazyportal.plugin.release.core.executor.CommandExecutor;
import org.eazyportal.plugin.release.core.executor.exception.CliExecutionException;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class JenkinsCliCommandExecutor implements CommandExecutor {

    private static final String[] OS_LINUX_COMMANDS = new String[] { "bash", "-c" };
    private static final String[] OS_WINDOWS_COMMANDS = new String[] { "cmd", "/c" };

    private final Launcher launcher;
    private final TaskListener listener;

    public JenkinsCliCommandExecutor(Launcher launcher, TaskListener listener) {
        this.launcher = Objects.requireNonNull(launcher);
        this.listener = Objects.requireNonNull(listener);
    }

    @NotNull
    @Override
    public String execute(@NotNull File workingDir, @NotNull String... commands) throws CliExecutionException {
        try (
            var stdOutOutputStream = new ByteArrayOutputStream();
            var stdErrOutputStream = new ByteArrayOutputStream()
        ) {
            var returnCode = launcher.launch()
                .cmds(enrichWithOsCommands(commands))
                .stdout(stdOutOutputStream)
                .stderr(stdErrOutputStream)
                .pwd(workingDir)
                .join();

            if (returnCode == 0) {
                String result = stdOutOutputStream.toString();

                listener.getLogger().println(result);

                return result;
            } else {
                String result = stdErrOutputStream.toString();

                listener.getLogger().println(result);

                throw new CliExecutionException(result);
            }
        } catch (IOException | InterruptedException exception) {
            listener.getLogger().println(exception.getMessage());

            throw new CliExecutionException(exception.getMessage());
        }
    }

    private static String[] enrichWithOsCommands(String... commands) {
        String[] osCommands;

        if (SystemUtils.IS_OS_WINDOWS) {
            osCommands = OS_WINDOWS_COMMANDS;
        } else {
            osCommands= OS_LINUX_COMMANDS;
        }

        return Stream.concat(Arrays.stream(osCommands), Arrays.stream(commands))
            .toArray(String[]::new);
    }

}
