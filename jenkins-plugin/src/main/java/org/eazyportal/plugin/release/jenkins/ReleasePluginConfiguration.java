package org.eazyportal.plugin.release.jenkins;

import hudson.Extension;
import org.eazyportal.plugin.release.gradle.project.GradleProjectActionsFactory;
import org.kohsuke.stapler.export.ExportedBean;

@ExportedBean
public class ReleasePluginConfiguration {

    @Extension
    public static GradleProjectActionsFactory projectActionsFactory() {
        return new GradleProjectActionsFactory();
    }

}
