package data;

import config.TestSmellMetricThresholds;
import it.unisa.testSmellDiffusion.testSmellRules.TestSmellMetric;

import java.util.Vector;

public class TestSmellsMetrics {
    private Vector<TestSmellMetric> arMetrics;
    private Vector<TestSmellMetric> etMetrics;
    private Vector<TestSmellMetric> gfMetrics;
    private Vector<TestSmellMetric> itMetrics;
    private Vector<TestSmellMetric> seMetrics;
    private Vector<TestSmellMetric> ltMetrics;
    private Vector<TestSmellMetric> mgMetrics;
    private Vector<TestSmellMetric> roMetrics;
    private Vector<TestSmellMetric> ftoMetrics;

    public TestSmellsMetrics(){
        ltMetrics = new Vector<TestSmellMetric>();
        ftoMetrics = new Vector<TestSmellMetric>();
    }

    public Vector<TestSmellMetric> getArMetrics() {
        return arMetrics;
    }

    public void setArMetrics(Vector<TestSmellMetric> arMetrics) {
        this.arMetrics = arMetrics;
    }

    public Vector<TestSmellMetric> getEtMetrics() {
        return etMetrics;
    }

    public void setEtMetrics(Vector<TestSmellMetric> etMetrics) {
        this.etMetrics = etMetrics;
    }

    public Vector<TestSmellMetric> getGfMetrics() {
        return gfMetrics;
    }

    public void setGfMetrics(Vector<TestSmellMetric> gfMetrics) {
        this.gfMetrics = gfMetrics;
    }

    public Vector<TestSmellMetric> getItMetrics() {
        return itMetrics;
    }

    public void setItMetrics(Vector<TestSmellMetric> itMetrics) {
        this.itMetrics = itMetrics;
    }

    public Vector<TestSmellMetric> getSeMetrics() {
        return seMetrics;
    }

    public void setSeMetrics(Vector<TestSmellMetric> seMetrics) {
        this.seMetrics = seMetrics;
    }

    public Vector<TestSmellMetric> getLtMetrics() {
        return ltMetrics;
    }

    public void setLtMetrics(Vector<TestSmellMetric> ltMetrics) {
        this.ltMetrics = ltMetrics;
    }

    public Vector<TestSmellMetric> getMgMetrics() {
        return mgMetrics;
    }

    public void setMgMetrics(Vector<TestSmellMetric> mgMetrics) {
        this.mgMetrics = mgMetrics;
    }

    public Vector<TestSmellMetric> getRoMetrics() {
        return roMetrics;
    }

    public void setRoMetrics(Vector<TestSmellMetric> roMetrics) {
        this.roMetrics = roMetrics;
    }

    public Vector<TestSmellMetric> getFtoMetrics() {
        return ftoMetrics;
    }

    public void setFtoMetrics(Vector<TestSmellMetric> ftoMetrics) {
        this.ftoMetrics = ftoMetrics;
    }

    public void addArMetric(TestSmellMetric metric){
        arMetrics.add(metric);
    }

    public void addEtMetric(TestSmellMetric metric){
        etMetrics.add(metric);
    }

    public void addRoMetric(TestSmellMetric metric){
        roMetrics.add(metric);
    }

    public void addFtoMetric(TestSmellMetric metric){
        ftoMetrics.add(metric);
    }

    public void addSeMetric(TestSmellMetric metric){
        seMetrics.add(metric);
    }

    public void addLtMetric(TestSmellMetric metric){
        ltMetrics.add(metric);
    }

    public void addMgMetric(TestSmellMetric metric){
        mgMetrics.add(metric);
    }

    public void addItMetric(TestSmellMetric metric){
        itMetrics.add(metric);
    }

    public void addGfMetric(TestSmellMetric metric){
        gfMetrics.add(metric);
    }

    public static double getMetricThreshold(String id, Vector<TestSmellMetricThresholds> thresholds, boolean critic){
        for(TestSmellMetricThresholds threshold : thresholds){
            if(threshold.getId().equalsIgnoreCase(id)){
                if(critic) return threshold.getGuardThreshold();
                return threshold.getDetectionThreshold();
            }
        }
        return Double.NaN;
    }

}
