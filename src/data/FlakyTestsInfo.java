package data;

import it.unisa.testSmellDiffusion.beans.MethodBean;

import java.util.ArrayList;
import java.util.Hashtable;

public class FlakyTestsInfo {
    private String testSuite;
    private ArrayList<MethodBean> flakyMethods;


    public FlakyTestsInfo() {
        flakyMethods=null;
    }

    public ArrayList<MethodBean> getFlakyMethods() {
        return flakyMethods;
    }

    public void setFlakyMethods(ArrayList<MethodBean> flakyMethods) {
        this.flakyMethods = flakyMethods;
    }

    public String getTestSuite() {
        return testSuite;
    }

    public void setTestSuite(String testSuite) {
        this.testSuite = testSuite;
    }


}
