package storage;

import config.TestSmellMetricThresholds;
import config.TestSmellMetricsThresholdsList;
import it.unisa.testSmellDiffusion.utility.FileUtility;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

public class ConfigFileManager {
    public TestSmellMetricsThresholdsList readThresholds(File file) {
        try {
            Ini ini = new Ini(file);
            ArrayList<TestSmellMetricThresholds> thresholdsList = new ArrayList<>();
            TestSmellMetricsThresholdsList list = new TestSmellMetricsThresholdsList();
            for (String sectionName : ini.keySet()) {
                if(sectionName.equalsIgnoreCase("codesmell")) break;
                    if (!sectionName.equalsIgnoreCase("testsmell") && !sectionName.equalsIgnoreCase("mutationsettings") && !sectionName.equalsIgnoreCase("flakytestssettings")) {
                        Profile.Section section = ini.get(sectionName);
                        TestSmellMetricThresholds arMetric = new TestSmellMetricThresholds();
                        Set<String> strings = section.keySet();
                        String[] metricValues = strings.toArray(new String[strings.size()]);
                        String id = sectionName;
                        String name = section.get(metricValues[0]);
                        String description = section.get(metricValues[1]);
                        double yellow = Double.parseDouble(section.get(metricValues[2]));
                        double red = Double.parseDouble(section.get(metricValues[3]));
                        String belongingSmellsString = section.get(metricValues[4]);
                        String belongingSmellsArray[] = belongingSmellsString.split(",");
                        ArrayList<TestSmellMetricThresholds.TestSmells> belongingSmells = new ArrayList<>();
                        for (String smellString : belongingSmellsArray) {
                            belongingSmells.add(TestSmellMetricThresholds.TestSmells.valueOf(smellString));
                        }
                        arMetric.setId(id);
                        arMetric.setName(name);
                        arMetric.setDescription(description);
                        arMetric.setDetectionThreshold(yellow);
                        arMetric.setGuardThreshold(red);
                        arMetric.setBelongingSmells(belongingSmells);
                        thresholdsList.add(arMetric);
                    }
                }

            list.setThresholds(thresholdsList);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public void writeThresholds(File file, ArrayList<TestSmellMetricThresholds> list) {
        String output = "";
        for (TestSmellMetricThresholds metric : list) {
            output += "[" + metric.getId() + "]\n";
            output += "name=" + metric.getName() + "\n";
            output += "description=" + metric.getDescription() + "\n";
            output += "detectionThreshold=" + metric.getDetectionThreshold() + "\n";
            output += "guardThreshold=" + metric.getGuardThreshold() + "\n";
            ArrayList<String> smellsStrings = new ArrayList<>();
            for (TestSmellMetricThresholds.TestSmells smell : metric.getBelongingSmells()) {
                smellsStrings.add(smell.name());
            }
            output += "belongingSmells=";
            for (int i = 0; i < smellsStrings.size() - 1; i++) {
                output += smellsStrings.get(i) + ",";
            }
            output += smellsStrings.get(smellsStrings.size() - 1);
            output+="\n\n";

            FileUtility.writeFile(output, file.getAbsolutePath());

        }
    }
}

