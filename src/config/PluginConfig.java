package config;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import gui.ConfigUI;
import it.unisa.testSmellDiffusion.utility.FileUtility;

import javax.swing.*;
import java.io.File;

public class PluginConfig extends AnAction {
    private static final Logger LOGGER = Logger.getInstance("global");
    @Override
    public void actionPerformed(AnActionEvent e) {
       // TestSmellMetricsThresholdsList list = new ConfigFileHandler().readThresholds(new File("C:\\Users\\Psycho\\IdeaProjects\\ProgettoExample\\default_config.ini"));
        String userDir = System.getProperty("user.home");
        String pluginFolder = userDir + "\\.temevi";
        LOGGER.info(System.getProperty("java.home"));
        File config = new File(pluginFolder + "\\default_config.ini");
        LOGGER.info(config.getAbsolutePath());
        if(!config.exists()){
            String output = "[NONDA]" +
                    "\nname=Number of Non-Documented Assertions " +
                    "\ndescription=Number of assert statements without a description " +
                    "\ndetectionThreshold=1.0 " +
                    "\nguardThreshold=3.0 " +
                    "\nbelongingSmells=ASSERTION_ROULETTE " +
                    "\n[APCMC]" +
                    "\nname=Average Production Class Methods Calls " +
                    "\ndescription=Number of production class' methods calls in the test suite, divided by the number of test methods " +
                    "\ndetectionThreshold=1.0 " +
                    "\nguardThreshold=3.0 " +
                    "\nbelongingSmells=EAGER_TEST " +
                    "\n[MEXR] " +
                    "\nname=Methods using External Resources " +
                    "\ndescription=Number of external resources uses made by test methods" +
                    "\ndetectionThreshold=1.0 " +
                    "\nguardThreshold=3.0 " +
                    "\nbelongingSmells=MYSTERY_GUEST " +
                    "\n[NEXEA] " +
                    "\nname=Number of EXternal resources Existence Assumptions " +
                    "\ndescription=Number of assumptions made in test methods about the existence of external resources (e.g. Files, Database) " +
                    "\ndetectionThreshold=1.0 " +
                    "\nguardThreshold=3.0 " +
                    "\nbelongingSmells=RESOURCE_OPTIMISM " +
                    "\n[GFMR]" +
                    "\nname=General Fixture Methods Rate " +
                    "\ndescription=The rate of test methods not using all the set-up variables defined " +
                    "\ndetectionThreshold=1.0 " +
                    "\nguardThreshold=3.0 " +
                    "\nbelongingSmells=GENERAL_FIXTURE " +
                    "\n[MTOOR] " +
                    "\nname=Methods Testing Other Objects Rate " +
                    "\ndescription=The rate of methods testing objects which are different from the production class " +
                    "\ndetectionThreshold=1.0 " +
                    "\nguardThreshold=3.0 " +
                    "\nbelongingSmells=INDIRECT_TESTING " +
                    "\n[TSEC] " +
                    "\nname=toString invocations in Equality Checks " +
                    "\ndescription=The number of toString invocations in equality checks " +
                    "\ndetectionThreshold=1.0 " +
                    "\nguardThreshold=3.0 " +
                    "\nbelongingSmells=SENSITIVE_EQUALITY";
            File plugin = new File(pluginFolder);
            plugin.mkdirs();
            FileUtility.writeFile(output, pluginFolder + "\\" + "default_config.ini");
        }
        JFrame frame = new ConfigUI(pluginFolder);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
