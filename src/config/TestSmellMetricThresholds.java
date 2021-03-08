package config;

import java.util.ArrayList;

public class TestSmellMetricThresholds {
    private String name;
    private String description;
    private String id;
    private double detectionThreshold;
    private double guardThreshold;
    private ArrayList<TestSmells> belongingSmells;

    public TestSmellMetricThresholds(String name, String description) {
        this.name = name;
        this.description = description;


    }

    public TestSmellMetricThresholds() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDetectionThreshold() {
        return detectionThreshold;
    }

    public void setDetectionThreshold(double detectionThreshold) {
        this.detectionThreshold = detectionThreshold;
    }

    public double getGuardThreshold() {
        return guardThreshold;
    }

    public void setGuardThreshold(double guardThreshold) {
        this.guardThreshold = guardThreshold;
    }

    public ArrayList<TestSmells> getBelongingSmells() {
        return belongingSmells;
    }

    public void setBelongingSmells(ArrayList<TestSmells> belongingSmells) {
        this.belongingSmells = belongingSmells;
    }

    public enum TestSmells{
        ASSERTION_ROULETTE, EAGER_TEST, GENERAL_FIXTURE, MYSTERY_GUEST, RESOURCE_OPTIMISM, INDIRECT_TESTING, SENSITIVE_EQUALITY
    }

    @Override
    public String toString() {
        return "TestSmellMetricThresholds{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", detectionThreshold=" + detectionThreshold +
                ", guardThreshold=" + guardThreshold +
                ", belongingSmells=" + belongingSmells +
                '}';
    }
}
