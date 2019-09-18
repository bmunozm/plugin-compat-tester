package org.jenkins.tools.test.hook;


import org.jenkins.tools.test.model.PomData;
import hudson.model.UpdateSite;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CloudbeesHaHook extends AbstractMultiParentHook {

    private static final Logger LOGGER = Logger.getLogger(CloudbeesHaHook.class.getName());

    @Override
    protected String getParentFolder() {
        return "jenkins-ha";
    }

    @Override
    protected String getParentUrl() {
        return "scm:git:ssh://git@github.com/cloudbees/jenkins-ha.git";
    }

    @Override
    protected String getParentProjectName() {
        return "jenkins-ha-parent";
    }

    @Override
    public boolean check(Map<String, Object> info) throws Exception {
        return isHaPlugin(info);
    }

    @Override
    protected String getPluginFolderName(UpdateSite.Plugin currentPlugin){
        return "management";
    }

    public static boolean isHaPlugin(Map<String, Object> moreInfo) {
        PomData data = (PomData) moreInfo.get("pomData");
        return isHaPlugin(data);
    }

    public static boolean isHaPlugin(PomData data) {
        return data.artifactId.contains("cloudbees-ha");
    }
}
