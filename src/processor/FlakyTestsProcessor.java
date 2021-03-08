package processor;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import data.FlakyTestsInfo;
import data.TestProjectAnalysis;
import init.PluginInit;
import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.beans.MethodBean;
import it.unisa.testSmellDiffusion.beans.PackageBean;
import it.unisa.testSmellDiffusion.main.Flaky;
import it.unisa.testSmellDiffusion.testMutation.TestMutationUtilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

public class FlakyTestsProcessor {
    private static final Logger LOGGER = Logger.getInstance("global");


    public static Vector<FlakyTestsInfo> calculate(TestProjectAnalysis proj, int times) {
        try {
            Vector<PackageBean> packages = proj.getPackages();
            Vector<PackageBean> testPackages = proj.getTestPackages();
            boolean isMaven = proj.isMaven();
            String javaLocation = proj.getJavaPath();
            String destination;
            String testPath;
            if (!isMaven) {
                destination = proj.getPath() + "\\out\\production\\" + proj.getName();
                testPath = proj.getPath() + "\\out\\test\\" + proj.getName();
            } else {
                destination = proj.getPath() + "\\target\\classes";
                testPath = proj.getPath() + "\\target\\test-classes";
            }
            TestMutationUtilities utilities = new TestMutationUtilities();
            ArrayList<ClassBean> classes = utilities.getClasses(packages);
            String pluginPath = proj.getPluginPath();
            Vector<FlakyTestsInfo> flakyTests = new Vector<>();
            Hashtable<String, Integer> passedTests;
            for (ClassBean productionClass : classes) {
                passedTests=new Hashtable<>();
                ClassBean testSuite = TestMutationUtilities.getTestClassBy(productionClass.getName(), testPackages);
                if (testSuite != null) {
                    String cmd = "\"" + javaLocation + "\" -cp " + pluginPath + "\\*;"
                            + destination + ";" + testPath +
                            " org.junit.runner.JUnitCore " + testSuite.getBelongingPackage() + "." + testSuite.getName();
                    Collection<MethodBean> methods = testSuite.getMethods();
                    FlakyTestsInfo info = new FlakyTestsInfo();
                    Hashtable<String, Integer> flaky = new Hashtable();
                    info.setTestSuite(testSuite.getName());
                    Runtime rt = Runtime.getRuntime();
                    //  LOGGER.info("STARTING FIRST RUN TESTS, CLASS nr." + j);
                    Process pr = rt.exec(cmd);
                    String s;
                    String output = "";
                    BufferedReader stdOut = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                    while ((s = stdOut.readLine()) != null) {
                        output += s;
                    }
                    stdOut.close();
                    BufferedReader stdErr = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
                    while ((s = stdErr.readLine()) != null) {
                    }
                    pr.waitFor();
                    for (MethodBean method : methods) {
                        flaky.put(method.getName(), 0);
                        if (output.contains(" " + method.getName() + "("))
                            passedTests.put(method.getName(), 0);
                        else
                            passedTests.put(method.getName(), 1);
                    }

                    //  LOGGER.info("FIRST RUN TESTS END, CLASS nr." + j);
                    for (int i = 0; i < times - 1; i++) {
                        pr = rt.exec(cmd);
                        s = "";
                        output = "";
                        stdOut = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                        while ((s = stdOut.readLine()) != null) {
                            output += s;
                        }
                        stdOut.close();

                        stdErr = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
                        while ((s = stdErr.readLine()) != null) {
                        }
                        pr.waitFor();

                        for (MethodBean method : methods) {
                            int isFlaky = flaky.get(method.getName());
                            if (isFlaky == 0) {
                                int passed = passedTests.get(method.getName());
                                if (output.contains(" " + method.getName() + "(") && passed == 1) {
                                    flaky.replace(method.getName(), 1);
                                    LOGGER.info("flaky detected");
                                } else if (!output.contains(" " + method.getName() + "(") && passed == 0) {
                                    flaky.replace(method.getName(), 1);
                                    LOGGER.info("flaky detected");
                                }
                            }
                        }
                        //   LOGGER.info("RUN TEST END: " + i+2);

                    }

                    ArrayList<MethodBean> flakyMethods = new ArrayList<>();
                    for (MethodBean method : methods) {
                        if (flaky.get(method.getName()) == 1)
                            flakyMethods.add(method);
                    }
                    info.setFlakyMethods(flakyMethods);
                    flakyTests.add(info);
                    //   LOGGER.info(flaky.toString());
                }
            }

            return flakyTests;

        } catch (Exception e) {
            LOGGER.info(e.toString());
            return null;
        }
    }
}
