package data;


import config.TestSmellMetricsThresholdsList;

public class ClassTestSmellsInfo {
    private TestSmellsMetrics metrics;
    private int assertionRoulette;
    private int eagerTest;
    private int lazyTest;
    private int mysteryGuest;
    private int sensitiveEquality;
    private int resourceOptimism;
    private int forTestersOnly;
    private int indirectTesting;
    private int generalFixture;
    private boolean isAffected;
    private boolean isAffectedCritic;
    private int weight;


    public ClassTestSmellsInfo() {
        weight = 1;
    }


    public TestSmellsMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(TestSmellsMetrics metrics) {
        this.metrics = metrics;
    }

    public int getAssertionRoulette() {
        return assertionRoulette;
    }

    public void setAssertionRoulette(int assertionRoulette) {
        this.assertionRoulette = assertionRoulette;
    }

    public int getEagerTest() {
        return eagerTest;
    }

    public void setEagerTest(int eagerTest) {
        this.eagerTest = eagerTest;
    }

    public int getLazyTest() {
        return lazyTest;
    }

    public void setLazyTest(int lazyTest) {
        this.lazyTest = lazyTest;
    }

    public int getMysteryGuest() {
        return mysteryGuest;
    }

    public void setMysteryGuest(int mysteryGuest) {
        this.mysteryGuest = mysteryGuest;
    }

    public int getSensitiveEquality() {
        return sensitiveEquality;
    }

    public void setSensitiveEquality(int sensitiveEquality) {
        this.sensitiveEquality = sensitiveEquality;
    }

    public int getResourceOptimism() {
        return resourceOptimism;
    }

    public void setResourceOptimism(int resourceOptimism) {
        this.resourceOptimism = resourceOptimism;
    }

    public int getForTestersOnly() {
        return forTestersOnly;
    }

    public void setForTestersOnly(int forTestersOnly) {
        this.forTestersOnly = forTestersOnly;
    }

    public int getIndirectTesting() {
        return indirectTesting;
    }

    public void setIndirectTesting(int indirectTesting) {
        this.indirectTesting = indirectTesting;
    }

    public int getGeneralFixture() {
        return generalFixture;
    }

    public void setGeneralFixture(int generalFixture) {
        this.generalFixture = generalFixture;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isAffected() {
        return isAffected;
    }

    public void setAffected(boolean affected) {
        isAffected = affected;
    }

    public boolean isAffectedCritic() {
        return isAffectedCritic;
    }

    public void setAffectedCritic(boolean affectedCritic) {
        isAffectedCritic = affectedCritic;
    }

    public void getCritValue(){

    }
}
