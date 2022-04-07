package org.eazyportal.plugin.release.jenkins.project;

import org.eazyportal.plugin.release.core.project.ProjectActions;
import org.eazyportal.plugin.release.core.project.exception.InvalidProjectLocationException;
import org.eazyportal.plugin.release.core.version.model.Version;

import java.io.File;

public class NodeProjectActions extends ProjectActions {

    public static final String PACKAGE_JSON_FILE_NAME = "package.json";

    private static final String VERSION_PATTERN = "\"version\": \"%s\"";
    private static final String VERSION_REGEX_PATTERN = "((?s).*)?\"version\"\\s?:\\s?\"(.+)?\"((?s).*)?";

    private final File packageJsonFile;

    public NodeProjectActions(File workingDir) throws InvalidProjectLocationException {
        if (!workingDir.exists() || workingDir.isFile()) {
            throw new InvalidProjectLocationException("Invalid Node.js project location: " + workingDir.getPath());
        }

        packageJsonFile = workingDir.toPath()
            .resolve(PACKAGE_JSON_FILE_NAME)
            .toFile();
    }

    @Override
    public Version getVersion() {
        return readVersion(packageJsonFile);
    }

    @Override
    public String[] scmFilesToCommit() {
        return new String[] {"."};
    }

    @Override
    public void setVersion(Version version) {
        writeVersion(packageJsonFile, version);
    }

    @Override
    protected String getNewVersionLine(String versionLine, Version version) {
        return versionLine.replaceFirst(VERSION_REGEX_PATTERN, "$1" + String.format(VERSION_PATTERN, version) + "$3");
    }

    @Override
    protected String getVersionFromLine(String versionLine) {
        return versionLine.replaceFirst(VERSION_REGEX_PATTERN, "$2");
    }

    @Override
    protected boolean isVersionLine(String line) {
        return line.matches(VERSION_REGEX_PATTERN);
    }

}
