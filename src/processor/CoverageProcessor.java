package processor;


//import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diagnostic.Logger;
import data.ClassCoverageInfo;
import data.TestProjectAnalysis;
import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.beans.PackageBean;
import it.unisa.testSmellDiffusion.metrics.CKMetrics;
import it.unisa.testSmellDiffusion.metrics.TestSmellMetrics;
import it.unisa.testSmellDiffusion.testMutation.TestMutationUtilities;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;


public class CoverageProcessor {
  private static final Logger LOGGER = Logger.getInstance("global");


    public static Vector<ClassCoverageInfo> calculate(TestProjectAnalysis proj) {
        try {
            double projectTotalLines = 0;
            double projectCoveredLines = 0;
            double projectTotalBranches = 0;
            double projectCoveredBranches = 0;
            String pluginPath = proj.getPluginPath();
            String jacocoCli = pluginPath + "\\jacococli.jar";
            String jacocoAgent = pluginPath + "\\jacocoagent.jar";
            Vector<ClassCoverageInfo> classCoverageInfo = new Vector<ClassCoverageInfo>();
            TestSmellMetrics testSmellMetrics = new TestSmellMetrics();
            TestMutationUtilities utilities = new TestMutationUtilities();
            Vector<PackageBean> testPackages = proj.getTestPackages();
            String javaPath = proj.getJavaPath();
            String configDir = proj.getConfigPath();
            ArrayList<ClassBean> classes = utilities.getClasses(proj.getPackages());
            double lineCoverage = -1.0d;
            double branchCoverage = -1.0d;
            double assertionDensity = Double.NaN;
            Hashtable<String, Integer> isGreenSuite = new Hashtable<>();
            String destination;
            String testPath;
            String buildPath;
            boolean isMaven = proj.isMaven();
            if (isMaven) {
                buildPath = proj.getPath() + "\\target";
                destination = proj.getPath() + "\\target\\classes";
                testPath = proj.getPath() + "\\target\\test-classes";
            } else {
                buildPath = proj.getPath() + "\\out";
                destination = proj.getPath() + "\\out\\production\\" + proj.getName();
                testPath = proj.getPath() + "\\out\\test\\" + proj.getName();
            }
            String cmd = "java -jar " + jacocoCli + " instrument " + destination + " --dest " + buildPath + "\\instrumented";

           // LOGGER.info("START COBERTURA INSTRUMENT");

            //LOGGER.info(cmd);
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(cmd);
            String s;
            String output = "";
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = stdOut.readLine()) != null) {
                output += s;
            }

            p.waitFor();
            //LOGGER.info("END COBERTURA INSTRUMENT");

          //  LOGGER.info(System.getProperty("user.dir"));

            //   LOGGER.info("" + classes.size());
            for (PackageBean packageBean : testPackages) {
                for (ClassBean testSuite : packageBean.getClasses()) {

                    //   ClassBean testSuite = TestMutationUtilities.getTestClassBy(productionClass.getName(), testPackages);
                    //  if (testSuite != null) {


                    cmd = "\"" + javaPath + "\" -cp " + jacocoAgent + ";" + configDir + ";" + pluginPath + "\\*;"
                            + buildPath + "\\instrumented;" + destination + ";" + testPath +
                            " org.junit.runner.JUnitCore " + testSuite.getBelongingPackage() + "." + testSuite.getName();
                   // LOGGER.info("START JUNIT TESTS");
                  //  LOGGER.info(cmd);
                    p = rt.exec(cmd);
                    output = "";
                    stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    while ((s = stdOut.readLine()) != null) {
                        output += s;
                    }
                    BufferedReader stdErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    output = "";
                    while ((s = stdErr.readLine()) != null) {
                        output += s;
                    }


                    p.waitFor();
                    //LOGGER.info("END JUNIT TESTS");

                }
            }


            cmd = "java -jar " + jacocoCli + " report " + configDir + "\\jacoco.exec" + " --classfiles " + destination + " --csv " + configDir + "\\coverage.csv";
           // LOGGER.info("START COBERTURA REPORT");
          //  LOGGER.info(cmd);
            rt = Runtime.getRuntime();
            p = rt.exec(cmd);
            output = "";
            stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = stdOut.readLine()) != null) {
                output += s;
            }
            p.waitFor();
           // LOGGER.info("END COBERTURA REPORT");

            cmd = "java -jar " + jacocoCli + " report " + configDir + "\\jacoco.exec" + " --classfiles " + destination + " --html " + configDir + "\\htmlCoverage";
          //  LOGGER.info("START COBERTURA REPORT");
         //   LOGGER.info(cmd);
            rt = Runtime.getRuntime();
            p = rt.exec(cmd);
            output = "";
            stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = stdOut.readLine()) != null) {
                output += s;
            }
            p.waitFor();
          //  LOGGER.info("END COBERTURA REPORT");

            for (ClassBean productionClass : classes) {
                ClassBean testSuite = TestMutationUtilities.getTestClassBy(productionClass.getName(), testPackages);
                if (testSuite != null) {

                    String line = "";
                    String cvsSplitBy = ",";
                    String reportPath = configDir + "\\coverage.csv";
                    File file = new File(reportPath);
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    br.readLine();
                    String[] data;
                    while ((line = br.readLine()) != null) {
                        data = line.split(cvsSplitBy);
                      //  LOGGER.info(data[1]);
                      //  LOGGER.info(data[2]);
                        if (data[1].equalsIgnoreCase(productionClass.getBelongingPackage()) && data[2].equalsIgnoreCase(productionClass.getName())) {
                            double coveredLines = Double.parseDouble(data[8]);
                            double missedLines = Double.parseDouble(data[7]);
                            double totalLines = coveredLines + missedLines;
                            double cov = coveredLines / totalLines;
                            lineCoverage = Math.round(cov * 100);
                            lineCoverage = lineCoverage / 100;
                            projectTotalLines += totalLines;
                            projectCoveredLines += coveredLines;
                         //   LOGGER.info("Line cov: " + lineCoverage );
                            double coveredBranches = Double.parseDouble(data[6]);
                            double missedBranches = Double.parseDouble(data[5]);
                            double totalBranches = coveredBranches + missedBranches;
                            if (totalBranches != 0) {
                                double branchCov = coveredBranches / totalBranches;
                                branchCoverage = Math.round(branchCov * 100);
                                branchCoverage = branchCoverage / 100;
                                projectTotalLines += totalLines;
                                projectCoveredLines += coveredLines;
                                projectTotalBranches += totalBranches;
                                projectCoveredBranches += coveredBranches;
                            } else
                                branchCoverage = -1.0d;
                        }
                    }
                    br.close();
                    ClassCoverageInfo coverageInfo = new ClassCoverageInfo();
                    coverageInfo.setName(testSuite.getName());
                    coverageInfo.setLineCoverage(lineCoverage);
                    coverageInfo.setBranchCoverage(branchCoverage);


                    int asserts = TestSmellMetrics.getNumberOfAsserts(testSuite);
                    int t_loc = CKMetrics.getLOC(testSuite);
                    double locdouble = (double) t_loc;
                    double assertsnr = (double) asserts;
                    double density = (assertsnr / locdouble) * 100;
                    assertionDensity = (double) Math.round(density) / 100;
                    coverageInfo.setAssertionDensity(assertionDensity);
                    classCoverageInfo.add(coverageInfo);
                }
            }

            //CLEANUP

            FileUtils.deleteDirectory(new File(buildPath + "\\instrumented"));



            double projectLineCov = projectCoveredLines / projectTotalLines;
            double projectLineCov100 = Math.round(projectLineCov * 100);
            projectLineCov100 = projectLineCov100 / 100;
            proj.setLineCoverage(projectLineCov100);

            double projectBranchCov = projectCoveredBranches / projectTotalBranches;
            double projectBranchCov100 = Math.round(projectBranchCov * 100);
            projectBranchCov100 = projectBranchCov100 / 100;
            proj.setBranchCoverage(projectBranchCov100);
            return classCoverageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}