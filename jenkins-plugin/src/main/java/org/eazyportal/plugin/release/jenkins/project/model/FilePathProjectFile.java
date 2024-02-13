package org.eazyportal.plugin.release.jenkins.project.model;

import hudson.FilePath;
import hudson.remoting.VirtualChannel;
import jenkins.MasterToSlaveFileCallable;
import org.eazyportal.plugin.release.core.project.model.ProjectFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public record FilePathProjectFile(FilePath filePath) implements ProjectFile<FilePath> {

    @Override
    public void createIfMissing() throws IOException, InterruptedException {
        if (!filePath.exists()) {
            filePath.write();
        }
    }

    @Override
    public boolean exists() throws IOException, InterruptedException {
        return filePath.exists();
    }

    @Override
    public FilePath getFile() {
        return filePath;
    }

    @Override
    public boolean isDirectory() throws IOException, InterruptedException {
        return filePath.isDirectory();
    }

    @Override
    public boolean isFile() throws IOException {
        return filePath.toVirtualFile().isFile();
    }

    @NotNull
    @Override
    public List<String> readLines() throws IOException, InterruptedException {
        return filePath.act(new ReadAllLines());
    }

    @NotNull
    @Override
    public String readText() throws IOException, InterruptedException {
        return filePath.readToString();
    }

    @NotNull
    @Override
    public FilePathProjectFile resolve(@NotNull String subPath) {
        return new FilePathProjectFile(filePath.child(subPath));
    }

    @Override
    public String toString() {
        try {
            return filePath.toURI().getPath();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void writeText(@NotNull String content) throws IOException, InterruptedException {
        filePath.write(content, Charset.defaultCharset().displayName());
    }

    private static class ReadAllLines extends MasterToSlaveFileCallable<List<String>> {

        @Override
        public List<String> invoke(File file, VirtualChannel channel) throws IOException, InterruptedException {
            return Files.readAllLines(file.toPath());
        }

    }

}
