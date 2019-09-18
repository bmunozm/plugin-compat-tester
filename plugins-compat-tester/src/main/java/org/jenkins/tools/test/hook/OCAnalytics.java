package org.jenkins.tools.test.hook;


import org.jenkins.tools.test.model.PomData;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OCAnalytics extends AbstractMultiParentHook {

    private static final Logger LOGGER = Logger.getLogger(OCAnalytics.class.getName());

    @Override
    protected String getParentFolder() {
        return "operations-center-analytics";
    }

    @Override
    protected String getParentUrl() {
        return "scm:git:ssh://git@github.com/cloudbees/operations-center-analytics.git";
    }

    @Override
    protected String getParentProjectName() {
        return "operations-center-analytics-project";
    }

    @Override
    public boolean check(Map<String, Object> info) throws Exception {
        return isOCAnalytics(info);
    }

    public static boolean isOCAnalytics(Map<String, Object> moreInfo) {
        PomData data = (PomData) moreInfo.get("pomData");
        return isOCAnalytics(data);
    }

    public static boolean isOCAnalytics(PomData data) {
        return data.artifactId.contains("operations-center-analytics") && data.parent != null && data.parent.artifactId.contains("operations-center-parent") ;
    }
}
