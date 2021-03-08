package data;

public class TestClassAnalysis {
    private String name;
    private String belongingPackage;
    private String productionClass;
    private ClassCKInfo ckMetrics;
    private ClassCoverageInfo coverage;
    private ClassMutationCoverageInfo mutationCoverage;
    private ClassTestSmellsInfo smells;
    private FlakyTestsInfo flakyTests;
    private double smellsThreshold;

    public TestClassAnalysis(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBelongingPackage() {
        return belongingPackage;
    }

    public void setBelongingPackage(String belongingPackage) {
        this.belongingPackage = belongingPackage;
    }

    public String getProductionClass() {
        return productionClass;
    }

    public void setProductionClass(String productionClass) {
        this.productionClass = productionClass;
    }

    public ClassCKInfo getCkMetrics() {
        return ckMetrics;
    }

    public void setCkMetrics(ClassCKInfo ckMetrics) {
        this.ckMetrics = ckMetrics;
    }

    public ClassCoverageInfo getCoverage() {
        return coverage;
    }

    public void setCoverage(ClassCoverageInfo coverage) {
        this.coverage = coverage;
    }

    public ClassTestSmellsInfo getSmells() {
        return smells;
    }

    public void setSmells(ClassTestSmellsInfo smells) {
        this.smells = smells;
       // this.smellsThreshold =  smells.getArMetric() + smells.getGfMetric() + smells.getItMetric() + smells.getFtoMetric() + smells.getRoMetric() + smells.getSeMetric()
         //       + smells.getMgMetric() + smells.getLtMetric() + smells.getEtMetric();
    }

    public FlakyTestsInfo getFlakyTests() {
        return flakyTests;
    }

    public void setFlakyTests(FlakyTestsInfo flakyTests) {
        this.flakyTests = flakyTests;
    }

    public double getSmellsThreshold(){
        return smellsThreshold;
    }

    public void setSmellsThreshold(double smellsThreshold) {
        this.smellsThreshold = smellsThreshold;
    }

    public ClassMutationCoverageInfo getMutationCoverage() {
        return mutationCoverage;
    }

    public void setMutationCoverage(ClassMutationCoverageInfo mutationCoverage) {
        this.mutationCoverage = mutationCoverage;
    }

    @Override
    public String toString() {
        return name;
    }
}
