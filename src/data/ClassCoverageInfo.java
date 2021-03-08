package data;

public class ClassCoverageInfo {
    private String name;
    private double lineCoverage;
    private double branchCoverage;
    private double assertionDensity;
    private double coveredLines;
    private double totalLines;


    public ClassCoverageInfo(String name, String belongingPackage, String productionClass, double lineCoverage, double branchCoverage, double mutationCoverage, double assertionDensity) {

        this.lineCoverage = lineCoverage;
        this.branchCoverage = branchCoverage;
        this.assertionDensity = assertionDensity;
    }

    public ClassCoverageInfo() {
        lineCoverage=-1.0d;
        branchCoverage=-1.0d;
    }


    public double getLineCoverage() {
        return lineCoverage;
    }

    public void setLineCoverage(double lineCoverage) {
        this.lineCoverage = lineCoverage;
    }

    public double getBranchCoverage() {
        return branchCoverage;
    }

    public void setBranchCoverage(double branchCoverage) {
        this.branchCoverage = branchCoverage;
    }


    public double getAssertionDensity() {
        return assertionDensity;
    }

    public void setAssertionDensity(double assertionDensity) {
        this.assertionDensity = assertionDensity;
    }


    public double getCoveredLines() {
        return coveredLines;
    }

    public void setCoveredLines(double coveredLines) {
        this.coveredLines = coveredLines;
    }

    public double getTotalLines() {
        return totalLines;
    }

    public void setTotalLines(double totalLines) {
        this.totalLines = totalLines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

