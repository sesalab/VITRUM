package utils;

import config.TestSmellMetricThresholds;
import data.ClassCoverageInfo;
import data.FlakyTestsInfo;

import java.util.Vector;

public class VectorFind {

    public static ClassCoverageInfo findCoverageInfo(Vector<ClassCoverageInfo> coverage, String name){
        for(ClassCoverageInfo info : coverage){
            if(info.getName().equalsIgnoreCase(name))
                return info;
        }
        return null;
    }

    public static FlakyTestsInfo findFlakyInfo(Vector<FlakyTestsInfo> flaky, String name){
        for(FlakyTestsInfo info : flaky){
            if(info.getTestSuite().equalsIgnoreCase(name))
                return info;
        }
        return null;
    }

   /* public static double findMaxValue(Vector<TestSmellMetricThresholds> metrics){
        double max=0;
        for(TestSmellMetricThresholds metric : metrics){
            if(metric.getValue() > max)
                max=metric.getValue();
        }
        return max;
    }

*/





}
