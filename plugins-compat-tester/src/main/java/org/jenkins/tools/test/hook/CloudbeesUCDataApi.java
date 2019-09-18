package org.jenkins.tools.test.hook;


import org.jenkins.tools.test.model.PomData;
import hudson.model.UpdateSite;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CloudbeesUCDataApi extends AbstractMultiParentHook {

    private static final Logger LOGGER = Logger.getLogger(CloudbeesUCDataApi.class.getName());

    @Override
    protected String getParentFolder() {
        return "cloudbees-uc-data";
    }

    @Override
    protected String getParentUrl() {
        return "scm:git:ssh://git@github.com/cloudbees/cloudbees-uc-data-plugin.git";
    }

    @Override
    protected String getParentProjectName() {
        return "cloudbees-uc-data";
    }

    @Override
    protected String getPluginFolderName(UpdateSite.Plugin currentPlugin){
        return "plugin";
    }

    @Override
    public boolean check(Map<String, Object> info) throws Exception {
        return isUC(info);
    }

    public static boolean isUC(Map<String, Object> moreInfo) {
        PomData data = (PomData) moreInfo.get("pomData");
        return isUC(data);
    }

    public static boolean isUC(PomData data) {
        return data.artifactId.contains("cloudbees-uc-data-api");
    }
}
