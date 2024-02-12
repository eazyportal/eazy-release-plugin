package org.eazyportal.plugin.release.jenkins;

import hudson.Extension;
import org.eazyportal.plugin.release.core.project.ProjectDescriptorFactory;
import org.eazyportal.plugin.release.core.version.ReleaseVersionProvider;
import org.eazyportal.plugin.release.core.version.SnapshotVersionProvider;
import org.eazyportal.plugin.release.core.version.VersionIncrementProvider;
import org.eazyportal.plugin.release.gradle.project.GradleProjectActionsFactory;
import org.kohsuke.stapler.export.ExportedBean;

@ExportedBean
public class ReleasePluginConfiguration {

    @Extension
    public static GradleProjectActionsFactory projectActionsFactory() {
        return new GradleProjectActionsFactory();
    }

    @Extension
    public static ProjectDescriptorFactory projectDescriptorFactory() {
        return new ProjectDescriptorFactory();
    }

    @Extension
    public static ReleaseVersionProvider releaseVersionProvider() {
        return new ReleaseVersionProvider();
    }

    @Extension
    public static SnapshotVersionProvider snapshotVersionProvider() {
        return new SnapshotVersionProvider();
    }

    @Extension
    public static VersionIncrementProvider versionIncrementProvider() {
        return new VersionIncrementProvider();
    }

}
