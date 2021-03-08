package init;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import data.*;
import gui.PluginInitGUI;
import it.unisa.testSmellDiffusion.beans.PackageBean;
import it.unisa.testSmellDiffusion.utility.FileUtility;
import it.unisa.testSmellDiffusion.utility.FolderToJavaProjectConverter;


import javax.swing.*;
import java.io.File;
import java.util.Vector;

public class PluginInit extends AnAction {
    private static final Logger LOGGER = Logger.getInstance("global");

    @Override
    public void actionPerformed(AnActionEvent e) {
        String userDir = System.getProperty("user.home");

        String pluginFolder = userDir + "\\.temevi";
        File config = new File(pluginFolder + "\\default_config.ini");
        File jacocoProp = new File(pluginFolder + "\\jacoco-agent.properties");
        if (!config.exists()) {
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
        if (!jacocoProp.exists()) {
            pluginFolder = pluginFolder.replace("\\", "\\\\");
            String output = "destfile = " + pluginFolder + "\\\\jacoco.exec";
            FileUtility.writeFile(output, pluginFolder + "\\" + "jacoco-agent.properties");
        }
        TestProjectAnalysis projectAnalysis = new TestProjectAnalysis();
        Project proj = e.getData(PlatformDataKeys.PROJECT);
        String projectFolder = proj.getBasePath();
        File root = new File(projectFolder);
        String srcPath = root.getAbsolutePath() + "/src";
        String mainPath = srcPath + "/main";
        String testPath = srcPath + "/test";
        File main = new File(mainPath);
        File test = new File(testPath);
        if (!main.exists() || !test.exists()) {
            JOptionPane.showMessageDialog(null, "PROJECT'S FOLDER STRUCTURE IS NOT CORRECT. PLEASE USE MAVEN DIRECTORY LAYOUT.");
        } else {
            boolean isMaven = false;
            for (File file : root.listFiles()) {
                if (file.isFile() && file.getName().equalsIgnoreCase("pom.xml"))
                    isMaven = true;
            }
            projectAnalysis.setMaven(isMaven);
            File project = new File(srcPath);
            String projectSDK = ProjectRootManager.getInstance(proj).getProjectSdk().getHomePath();
            String javaPath = projectSDK + "/bin/java.exe";
            projectAnalysis.setName(proj.getName());
            projectAnalysis.setPath(proj.getBasePath());
            projectAnalysis.setJavaPath(javaPath);
            Vector<TestClassAnalysis> classAnalysis = new Vector<>();
            if ((test.isDirectory()) && (!test.isHidden())) {
                try {
                    Vector<PackageBean> testPackages = FolderToJavaProjectConverter.convert(test.getAbsolutePath());
                    if (testPackages.size() == 1 && testPackages.get(0).getClasses().size() == 0)
                        JOptionPane.showMessageDialog(null, "TESTING SOURCE FILES NOT FOUND");
                    else {
                        Vector<PackageBean> packages = FolderToJavaProjectConverter.convert(mainPath);
                        projectAnalysis.setPackages(packages);
                        projectAnalysis.setTestPackages(testPackages);
                        projectAnalysis.setConfigPath(System.getProperty("user.home") + "\\.temevi");
                        PluginInitGUI initGUI = new PluginInitGUI(projectAnalysis);

                    } } catch(Exception ex){
                        ex.printStackTrace();
                    }


            }
        }
    }


}




