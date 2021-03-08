package processor;

import com.intellij.openapi.diagnostic.Logger;
import config.TestSmellMetricThresholds;
import storage.ConfigFileManager;
import config.TestSmellMetricsThresholdsList;
import data.ClassTestSmellsInfo;
import data.TestProjectAnalysis;
import data.TestSmellsMetrics;
import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.beans.MethodBean;
import it.unisa.testSmellDiffusion.beans.PackageBean;
import it.unisa.testSmellDiffusion.testSmellRules.*;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

public class SmellynessProcessor {
    private static final Logger LOGGER = Logger.getInstance("global");

    public static ClassTestSmellsInfo calculate(ClassBean testSuite, ClassBean productionClass, Vector<PackageBean> packages, TestProjectAnalysis project) {
        try {
            boolean isAffected=false;
            boolean isCritic=false;
            Collection<MethodBean> methodsInTheProject = IndirectTesting.findInvocations(packages);
            AssertionRoulette assertionRoulette = new AssertionRoulette();
            EagerTest eagerTest = new EagerTest();
            LazyTest lazyTest = new LazyTest();
            MysteryGuest mysteryGuest = new MysteryGuest();
            SensitiveEquality sensitiveEquality = new SensitiveEquality();
            ResourceOptimistism resourceOptimism = new ResourceOptimistism();
            ForTestersOnly forTestersOnly = new ForTestersOnly();
            IndirectTesting indirectTesting = new IndirectTesting();
            GeneralFixture generalFixture = new GeneralFixture();
            ClassTestSmellsInfo  classTestSmellsInfo = new ClassTestSmellsInfo();
            TestSmellMetricsThresholdsList metricsList;
           File default_conf = new File(System.getProperty("user.home") + "\\.temevi" + "\\default_config.ini");
           System.out.println(System.getProperty("user.home"));
           File conf = new File(System.getProperty("user.home") + "\\.temevi" + "\\config.ini");
       /*if(!default_conf.exists()) {
            thresholds = new SmellsThresholds(1,1,1,1,1,1,1,1,1);
            new ConfigFileHandler().writeThresholds(new File(projdir + "\\default_config.ini"), thresholds);
        }*/
            if(conf.exists())
                metricsList = new ConfigFileManager().readThresholds(conf);

            else
                metricsList = new ConfigFileManager().readThresholds(default_conf);

        //    classTestSmellsInfo.set
            if(metricsList==null) throw new NullPointerException();
                boolean isAssertionRoulette;
                boolean isEagerTest;
            boolean isLazyTest;
            boolean isMysteryGuest;
            boolean isSensitiveEquality;
            boolean isResourceOptimistism;
            boolean isForTestersOnly;
            boolean isIndirectTesting;
            boolean isGeneralFixture;
                String testSuiteName = "NO-TEST";
            TestSmellsMetrics metrics = new TestSmellsMetrics();
                if (testSuite != null) {
                    TestSmellMetricThresholds threshold = metricsList.getThresholdsById("NONDA");
                    isAssertionRoulette = assertionRoulette.isAssertionRoulette(testSuite, threshold.getDetectionThreshold());
                    if (isAssertionRoulette) {
                        System.out.println("Is AR");
                        classTestSmellsInfo.setAssertionRoulette(1);
                        isAffected=true;
                        for(TestSmellMetric metric : assertionRoulette.getMetrics())
                            if(metric.getValue() >= threshold.getGuardThreshold()) {
                                isCritic = true;
                                classTestSmellsInfo.setAssertionRoulette(2);
                            }


                            }
                    metrics.setArMetrics(assertionRoulette.getMetrics());

                     threshold = metricsList.getThresholdsById("APCMC");
                    isEagerTest = eagerTest.isEagerTest(testSuite, productionClass, threshold.getDetectionThreshold());
                    if (isEagerTest) {
                        LOGGER.info("Is ET");

                        classTestSmellsInfo.setEagerTest(1);
                        isAffected=true;
                        for(TestSmellMetric metric : eagerTest.getMetrics())
                            if(metric.getValue() >= threshold.getGuardThreshold()) {
                                isCritic = true;
                                classTestSmellsInfo.setEagerTest(2);
                            }

                            }
                    metrics.setEtMetrics(eagerTest.getMetrics());


                    threshold = metricsList.getThresholdsById("MEXR");
                    isMysteryGuest = mysteryGuest.isMysteryGuest(testSuite, threshold.getDetectionThreshold());
                    if (isMysteryGuest) {
                        LOGGER.info("Is MG");

                        classTestSmellsInfo.setMysteryGuest(1);
                        isAffected=true;
                        for(TestSmellMetric metric : mysteryGuest.getMetrics())
                            if(metric.getValue() >= threshold.getGuardThreshold()) {
                                isCritic = true;
                                classTestSmellsInfo.setMysteryGuest(1);

                            }

                    }
                    metrics.setMgMetrics(mysteryGuest.getMetrics());

                    threshold = metricsList.getThresholdsById("TSEC");
                    isSensitiveEquality = sensitiveEquality.isSensitiveEquality(testSuite, threshold.getDetectionThreshold());
                    if (isSensitiveEquality) {
                        LOGGER.info("Is SE");

                        classTestSmellsInfo.setSensitiveEquality(1);
                        isAffected=true;
                        for(TestSmellMetric metric : sensitiveEquality.getMetrics())
                            if(metric.getValue() >= threshold.getGuardThreshold()) {
                                isCritic = true;
                                classTestSmellsInfo.setSensitiveEquality(2);

                            }

                    }
                    metrics.setSeMetrics(sensitiveEquality.getMetrics());

                    threshold = metricsList.getThresholdsById("NEXEA");
                    isResourceOptimistism = resourceOptimism.isResourceOptimistism(testSuite, threshold.getDetectionThreshold());
                    if (isResourceOptimistism) {
                        LOGGER.info("Is RO");

                        classTestSmellsInfo.setResourceOptimism(1);
                        isAffected=true;
                        for(TestSmellMetric metric : resourceOptimism.getMetrics())
                            if(metric.getValue() >= threshold.getGuardThreshold()) {
                                isCritic = true;
                                classTestSmellsInfo.setResourceOptimism(2);

                            }

                    }
                    metrics.setRoMetrics(resourceOptimism.getMetrics());


                    threshold = metricsList.getThresholdsById("MTOOR");
                    isIndirectTesting = indirectTesting.isIndirectTesting(testSuite, productionClass, methodsInTheProject, threshold.getDetectionThreshold());
                    if (isIndirectTesting) {
                        LOGGER.info("Is IT");

                        classTestSmellsInfo.setIndirectTesting(1);
                        isAffected=true;
                        for(TestSmellMetric metric : indirectTesting.getMetrics())
                            if(metric.getValue() >= threshold.getGuardThreshold()) {
                                isCritic = true;
                                classTestSmellsInfo.setIndirectTesting(2);
                            }

                    }
                    metrics.setItMetrics(indirectTesting.getMetrics());

                    threshold = metricsList.getThresholdsById("GFMR");
                    isGeneralFixture = generalFixture.isGeneralFixture(testSuite, threshold.getDetectionThreshold());
                    if (isGeneralFixture) {
                        LOGGER.info("Is GF");

                        classTestSmellsInfo.setGeneralFixture(1);
                        isAffected=true;
                        for(TestSmellMetric metric : generalFixture.getMetrics())
                            if(metric.getValue() >= threshold.getGuardThreshold()) {
                                isCritic = true;
                                classTestSmellsInfo.setGeneralFixture(2);

                            }

                    }
                    metrics.setGfMetrics(generalFixture.getMetrics());



            }

            if(isAffected) {
                project.setAffectedClasses(project.getAffectedClasses() + 1);
                classTestSmellsInfo.setAffected(true);
                classTestSmellsInfo.setWeight(2);
            }
            if(isCritic) {
                classTestSmellsInfo.setAffectedCritic(true);
                classTestSmellsInfo.setWeight(3);
            }
            classTestSmellsInfo.setMetrics(metrics);
            return classTestSmellsInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
