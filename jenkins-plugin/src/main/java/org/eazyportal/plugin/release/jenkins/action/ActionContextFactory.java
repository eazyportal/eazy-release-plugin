package org.eazyportal.plugin.release.jenkins.action;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.InvisibleAction;
import org.eazyportal.plugin.release.core.action.model.ActionContext;

import java.io.Serializable;

@Extension
public class ActionContextFactory extends InvisibleAction implements Serializable {

    public ActionContext create(EnvVars env) {
        return new ActionContext(
            Boolean.getBoolean(env.get("FORCE_RELEASE", "false"))
        );
    }

}
