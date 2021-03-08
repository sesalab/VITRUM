package config;

import java.util.ArrayList;
import java.util.Vector;

public class TestSmellMetricsThresholdsList {
    private ArrayList<TestSmellMetricThresholds> thresholds;

    public TestSmellMetricsThresholdsList(){}

    public TestSmellMetricsThresholdsList(ArrayList<TestSmellMetricThresholds> thresholds) {
        this.thresholds = thresholds;
    }

    public ArrayList<TestSmellMetricThresholds> getThresholds() {
        return thresholds;
    }

    public void setThresholds(ArrayList<TestSmellMetricThresholds> thresholds) {
        this.thresholds = thresholds;
    }

    public TestSmellMetricThresholds getThresholdsById(String id){
        for(TestSmellMetricThresholds threshold : this.thresholds){
            if(threshold.getId().equalsIgnoreCase(id))
                return threshold;
        }
        return null;
    }

    public static TestSmellMetricThresholds getThresholdsById(String id, Vector<TestSmellMetricThresholds> thresholds){
        for(TestSmellMetricThresholds threshold : thresholds){
            if(threshold.getId().equalsIgnoreCase(id))
                return threshold;
        }
        return null;
    }

    public Vector<TestSmellMetricThresholds> getThresholdsBySmell(TestSmellMetricThresholds.TestSmells belongingSmell){
        Vector<TestSmellMetricThresholds> thresholdsFound = new Vector<>();
        for(TestSmellMetricThresholds threshold : this.thresholds){
            if(threshold.getBelongingSmells().contains(belongingSmell))
                thresholdsFound.add(threshold);
        }
        return thresholdsFound;
    }
}
