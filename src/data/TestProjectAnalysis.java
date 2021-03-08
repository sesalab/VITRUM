package data;

import it.unisa.testSmellDiffusion.beans.PackageBean;

import java.util.ArrayList;
import java.util.Vector;

public class TestProjectAnalysis {
    private String name;
    private String path;
    private boolean isMaven;
    private int testClassesNumber;
    private double lineCoverage;
    private double branchCoverage;
    private String pluginPath;
    private String javaPath;
    private String configPath;
    private int loc;
    private int nom;
    private int rfc;
    private int wmc;
    private int affectedClasses;
    private Vector<TestClassAnalysis> classAnalysis;
    private Vector<PackageBean> packages;
    private Vector<PackageBean> testPackages;


    public String getPluginPath() {
        return pluginPath;
    }

    public String getJavaPath() {
        return javaPath;
    }

    public boolean isMaven() {
        return isMaven;
    }

    public void setMaven(boolean maven) {
        isMaven = maven;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public Vector<PackageBean> getPackages() {
        return packages;
    }

    public void setPackages (Vector<PackageBean> packages) {
        this.packages = packages;
    }

    public Vector<PackageBean> getTestPackages() {
        return testPackages;
    }

    public void setTestPackages(Vector<PackageBean> testPackages) {
        this.testPackages = testPackages;
    }

    public void setJavaPath(String javaPath) {
        this.javaPath = javaPath;
    }

    public void setPluginPath(String pluginPath) {
        this.pluginPath = pluginPath;
    }

    public TestProjectAnalysis() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTestClassesNumber() {
        return testClassesNumber;
    }

    public void setTestClassesNumber(int testClassesNumber) {
        this.testClassesNumber = testClassesNumber;
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

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public int getNom() {
        return nom;
    }

    public void setNom(int nom) {
        this.nom = nom;
    }

    public int getRfc() {
        return rfc;
    }

    public void setRfc(int rfc) {
        this.rfc = rfc;
    }

    public int getWmc() {
        return wmc;
    }

    public void setWmc(int wmc) {
        this.wmc = wmc;
    }

    public Vector<TestClassAnalysis> getClassAnalysis() {
        return classAnalysis;
    }

    public void setClassAnalysis(Vector<TestClassAnalysis> classAnalysis) {
        this.classAnalysis = classAnalysis;
    }

    public int getAffectedClasses() {
        return affectedClasses;
    }

    public void setAffectedClasses(int affectedClasses) {
        this.affectedClasses = affectedClasses;
    }

    public String getPath() {
        return path;
    }

    public Vector<TestClassAnalysis> getClassAffectedBySmell(int smellIndex) {
        Vector<TestClassAnalysis> classes = new Vector<>();
        switch (smellIndex) {
            case 0:
                return this.classAnalysis;
            case 1:
                for (TestClassAnalysis analysis : classAnalysis) {
                    if (analysis.getSmells().getAssertionRoulette() == 1 || analysis.getSmells().getAssertionRoulette()==2)
                        classes.add(analysis);
                }
                break;
            case 2:
                for (TestClassAnalysis analysis : classAnalysis) {
                    if (analysis.getSmells().getEagerTest() == 1||analysis.getSmells().getEagerTest()==2)
                        classes.add(analysis);
                }
                break;

            case 3:
                for (TestClassAnalysis analysis : classAnalysis) {
                    if (analysis.getSmells().getGeneralFixture() == 1||analysis.getSmells().getGeneralFixture()==2)
                        classes.add(analysis);

                }
                break;

            case 4:
                for (TestClassAnalysis analysis : classAnalysis) {
                    if (analysis.getSmells().getSensitiveEquality() == 1||analysis.getSmells().getSensitiveEquality()==2)
                        classes.add(analysis);
                }
                break;

            case 5:
                for (TestClassAnalysis analysis : classAnalysis) {
                    if (analysis.getSmells().getMysteryGuest() == 1||analysis.getSmells().getMysteryGuest()==2)
                        classes.add(analysis);
                }
                break;

            case 6:
                for (TestClassAnalysis analysis : classAnalysis) {
                    if (analysis.getSmells().getIndirectTesting() == 1||analysis.getSmells().getIndirectTesting()==2)
                        classes.add(analysis);
                }
                break;

            case 7:
                for (TestClassAnalysis analysis : classAnalysis) {
                    if (analysis.getSmells().getResourceOptimism() == 1||analysis.getSmells().getResourceOptimism()==2)
                        classes.add(analysis);
                }
                break;


        }
        return classes;
    }

                public void setPath (String path){
                this.path = path;
            }
        }
