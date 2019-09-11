package org.jenkins.tools.test.hook;

import hudson.model.UpdateSite;
import hudson.util.VersionNumber;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;
import org.jenkins.tools.test.SCMManagerFactory;
import org.jenkins.tools.test.model.PluginCompatTesterConfig;
import org.jenkins.tools.test.model.PomData;
import org.jenkins.tools.test.model.comparators.VersionComparator;
import org.jenkins.tools.test.model.hook.PluginCompatTesterHookBeforeCheckout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class OpenshiftHook extends PluginCompatTesterHookBeforeCheckout {

    @Override
    public boolean check(Map<String, Object> info) throws Exception {
        UpdateSite.Plugin plugin = (UpdateSite.Plugin) info.get("plugin");
        return plugin.name.equalsIgnoreCase("openshift-cli");
    }

    @Override
    public Map<String, Object> action(Map<String, Object> info) throws Exception {
        PluginCompatTesterConfig config = (PluginCompatTesterConfig)info.get("config");
        PomData pomData = (PomData)info.get("pomData");
        UpdateSite.Plugin plugin = (UpdateSite.Plugin) info.get("plugin");

        // We should not execute the hook if using localCheckoutDir
        boolean shouldExecuteHook = config.getLocalCheckoutDir() == null || !config.getLocalCheckoutDir().exists();

        if (shouldExecuteHook) {
            System.out.println("Executing " + this.getClass().getSimpleName() + " for " + pomData.artifactId);

            // Checkout to the parent directory. All other processes will be on the child directory
            File checkoutPath = new File(config.workDirectory.getAbsolutePath() + "/" + pomData.artifactId);

            String scmTag =  pomData.artifactId.toString() + '-' + plugin.version;
            System.out.println("Checking out from SCM tag " + scmTag);
                
            ScmManager scmManager = SCMManagerFactory.getInstance().createScmManager();
            ScmRepository repository = scmManager.makeScmRepository("scm:git:https://github.com/cloudbees/openshift-cli-plugin.git");
            CheckOutScmResult result = scmManager.checkOut(repository, new ScmFileSet(checkoutPath), new ScmTag(scmTag));

            if (!result.isSuccess()) {
                // Throw an exception if there are any download errors.
                throw new RuntimeException(result.getProviderMessage() + "||" + result.getCommandOutput());
            }


            // Checkout already happened, don't run through again
            info.put("runCheckout", false);

            info.put("checkoutDir", checkoutPath);
            info.put("pluginDir", checkoutPath);
        }
        return info;

    }
}
