package data;

public class ClassMutationCoverageInfo {
    private double mutationCoverage;
    private double mutatedLines;
    private double coveredMutatedLines;
    private String reportName;

    public ClassMutationCoverageInfo(double mutationCoverage, double mutatedLines, double coveredMutatedLines) {
        this.mutationCoverage = mutationCoverage;
        this.mutatedLines = mutatedLines;
        this.coveredMutatedLines = coveredMutatedLines;
    }

    public ClassMutationCoverageInfo(){
        mutationCoverage = -1.0d;
    }

    public double getMutationCoverage() {
        return mutationCoverage;
    }

    public void setMutationCoverage(double mutationCoverage) {
        this.mutationCoverage = mutationCoverage;
    }

    public double getMutatedLines() {
        return mutatedLines;
    }

    public void setMutatedLines(double mutatedLines) {
        this.mutatedLines = mutatedLines;
    }

    public double getCoveredMutatedLines() {
        return coveredMutatedLines;
    }

    public void setCoveredMutatedLines(double coveredMutatedLines) {
        this.coveredMutatedLines = coveredMutatedLines;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
}
