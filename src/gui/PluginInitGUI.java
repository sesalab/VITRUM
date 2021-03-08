package gui;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import data.*;
import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.beans.PackageBean;
import it.unisa.testSmellDiffusion.metrics.CKMetrics;
import it.unisa.testSmellDiffusion.testMutation.TestMutationUtilities;
import org.apache.commons.io.FileUtils;
import processor.*;
import storage.ReportManager;
import utils.CommandOutput;
import utils.VectorFind;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

public class PluginInitGUI extends JFrame {
    private TestProjectAnalysis project;
    private JCheckBox ckMetrics, flakyTests, mutationCoverage, lineBranchCoverage, codeSmells;
    private JLabel flakyTestsExecutions, mutationCoverageTimeout;
    private JSpinner ftExecNumber, mcTimeout;
    private JButton editMetricsThresholds, runAnalysis;
    private JFrame initFrame, metricsThresholdFrame;

    public void addComponents(Container pane) {
        pane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // CheckBox ckMetrics COL1 - ROW1 1[x--]
        ckMetrics = new JCheckBox("Test Smells", true);
        ckMetrics.setEnabled(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        pane.add(ckMetrics, constraints);
        // CheckBox FlakyTests COL1 - ROW2 2[x--]
        flakyTests = new JCheckBox("Flaky Tests", false);
        flakyTests.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (flakyTests.isSelected()) {
                    ftExecNumber.setEnabled(true);
                } else {
                    ftExecNumber.setEnabled(false);
                }
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        pane.add(flakyTests, constraints);
        // Label FlakyTestsExecutions COL2 - ROW2 2[-x-]
        flakyTestsExecutions = new JLabel("Numbers of Flaky Tests Executions:");
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        pane.add(flakyTestsExecutions, constraints);
        // JSpinner FlakyTestsExecutions COL3 - ROW2 2[--x]
        SpinnerModel ftSpinnerModel = new SpinnerNumberModel(10, 10, 50, 1);
        ftExecNumber = new JSpinner(ftSpinnerModel);
        ftExecNumber.setEnabled(false);
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        pane.add(ftExecNumber, constraints);
        // CheckBox Mutation Coverage COL1 - ROW3 3[x--]
        mutationCoverage = new JCheckBox("Mutation Coverage", false);
        mutationCoverage.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (mutationCoverage.isSelected()) {
                    mcTimeout.setEnabled(true);
                } else {
                    mcTimeout.setEnabled(false);
                }
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_START;
        pane.add(mutationCoverage, constraints);
        // Label MutationCoverage Timeout COL2 - ROW3 3[-x-]
        mutationCoverageTimeout = new JLabel("Mutation Coverage Timeout:");
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_END;
        pane.add(mutationCoverageTimeout, constraints);
        // JSpinner MutationCoverageTimeout COL3 - ROW3 3[--x]
        Long val = 10L;//set your own value, I used to check if it works
        Long min = 5L;
        Long max = 1000L;
        Long step = 1L;
        SpinnerModel mcSpinnerModel = new SpinnerNumberModel(val, min, max, step);
        mcTimeout = new JSpinner(mcSpinnerModel);
        mcTimeout.setEnabled(false);
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_START;
        pane.add(mcTimeout, constraints);
        // CheckBox LineBranchCoverage COL1 - ROW4 4[x--]
        lineBranchCoverage = new JCheckBox("Line and Branch Coverage");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.LINE_START;
        pane.add(lineBranchCoverage, constraints);

        // Button editMetricsThresholds COL2 - ROW6 6[-x-]

        editMetricsThresholds = new JButton("Edit Metrics Thresholds");
        editMetricsThresholds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String pluginFolder = System.getProperty("user.home") + "\\.temevi";
                JFrame frame = new ConfigUI(pluginFolder);
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });

        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.insets = new Insets(10, 0, 10, 0);
        pane.add(editMetricsThresholds, constraints);
        // Button runAnalysis COL3 - ROW6 6[--x]
        runAnalysis = new JButton("Start Analysis");
        runAnalysis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Thread() {
                    public void run() {
                        initFrame.setVisible(false);
                        JFrame frame = swingProgressBar();
                        String pluginPath = PathManager.getPluginsPath() + "\\TestFactorsPlugin\\lib";
                        project.setPluginPath(pluginPath);
                        Vector<PackageBean> packages = project.getPackages();
                        Vector<PackageBean> testPackages = project.getTestPackages();
                        TestMutationUtilities utils = new TestMutationUtilities();
                        ArrayList<ClassBean> classes = utils.getClasses(packages);
                        Vector<ClassCoverageInfo> coverageInfos = null;
                        Vector<FlakyTestsInfo> flakyInfos = null;
                        Vector<TestClassAnalysis> classAnalyses = new Vector<>();
                        /*boolean ok = true;
                        if (lineBranchCoverage.isSelected() || flakyTests.isSelected() || mutationCoverage.isSelected()) {
                            File bytecode = new File(project.getPath() + "\\out");
                            File mavenBytecode = new File(project.getPath() + "\\target\\classes");
                            File mavenTestcode = new File(project.getPath() + "\\target\\test-classes");
                            if (isMaven && (!mavenBytecode.exists() || !mavenTestcode.exists()))
                                ok = false;
                            else if (!isMaven && !bytecode.exists())
                                ok = false;
                        }
                        if (ok) {*/

                        if (lineBranchCoverage.isSelected()) {
                            String configDir = System.getProperty("user.home") + "\\.temevi";
                            coverageInfos = CoverageProcessor.calculate(project);
                        }
                        if (flakyTests.isSelected()) {
                            flakyInfos = FlakyTestsProcessor.calculate(project, (int) ftExecNumber.getValue());
                        }
                        for (ClassBean prodClass : classes) {
                            ClassBean testSuite = utils.getTestClassBy(prodClass.getName(), testPackages);
                            if (testSuite != null) {
                                TestClassAnalysis analysis = new TestClassAnalysis();
                                analysis.setName(testSuite.getName());
                                analysis.setBelongingPackage(testSuite.getBelongingPackage());
                                analysis.setProductionClass(prodClass.getBelongingPackage() + "." + prodClass.getName());
                                int loc = CKMetrics.getLOC(testSuite);
                                int nom = CKMetrics.getNOM(testSuite);
                                int wmc = CKMetrics.getWMC(testSuite);
                                int rfc = CKMetrics.getRFC(testSuite);
                                ClassCKInfo classInfo = new ClassCKInfo(loc, rfc, nom, wmc);
                                project.setLoc(project.getLoc() + loc);
                                project.setNom(project.getNom() + nom);
                                project.setTestClassesNumber(project.getTestClassesNumber() + 1);
                                analysis.setCkMetrics(classInfo);
                                analysis.setSmells(SmellynessProcessor.calculate(testSuite, prodClass, packages, project));
                                if (coverageInfos != null) {
                                    analysis.setCoverage(VectorFind.findCoverageInfo(coverageInfos, testSuite.getName()));
                                } else {
                                    analysis.setCoverage(new ClassCoverageInfo());
                                }
                                if (mutationCoverage.isSelected()) {
                                    analysis.setMutationCoverage(MutationCoverageProcessor.calculate(testSuite, prodClass, project, (Long) mcTimeout.getValue()));
                                } else if (!mutationCoverage.isSelected())
                                    analysis.setMutationCoverage(new ClassMutationCoverageInfo());

                                if (flakyTests.isSelected())
                                    analysis.setFlakyTests(VectorFind.findFlakyInfo(flakyInfos, testSuite.getName()));
                                else
                                    analysis.setFlakyTests(new FlakyTestsInfo());
                                classAnalyses.add(analysis);
                            }
                        }
                        frame.dispose();
                        project.setClassAnalysis(classAnalyses);
                        ReportManager.saveReport(project);

                        JFrame ckShow = new AnalysisResultsUI(project);
                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        ckShow.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent e) {
                                super.windowClosing(e);
                                JFrame frame = (JFrame) e.getSource();
                                try {
                                    FileUtils.deleteDirectory(new File(System.getProperty("user.home") + "\\.temevi" + "\\htmlCoverage"));
                                    FileUtils.forceDelete(new File(System.getProperty("user.home") + "\\.temevi" + "\\coverage.csv"));
                                    FileUtils.forceDelete(new File(System.getProperty("user.home") + "\\.temevi" + "\\jacoco.exec"));
                                    FileUtils.deleteDirectory(new File(System.getProperty("user.home") + "\\.temevi\\pitreport"));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            }
                        });
                        ckShow.setVisible(true);
                       /* } else {
                            frame.dispose();
                            JOptionPane.showMessageDialog(null, "IL PROGETTO NON CONTIENE PRODUCTION CLASSES E TEST CLASSES COMPILATE!");

                        } */

                    }

                }.start();
            }
        });


        constraints.gridx = 2;
        constraints.gridy = 5;
        constraints.insets = new Insets(10, 0, 10, 10);
        pane.add(runAnalysis, constraints);

        // constraints.fill = GridBagConstraints.HORIZONTAL; // natural height, maximum width
    }


    public PluginInitGUI(TestProjectAnalysis project) {
        this.project=project;
        initFrame = new JFrame("VITRuM");
        initFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addComponents(initFrame.getContentPane());
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        initFrame.setLocation(dimension.width / 2 - initFrame.getSize().width / 2, dimension.height / 2 - initFrame.getSize().height / 2);
        initFrame.pack();
        initFrame.setVisible(true);
    }

    public JFrame swingProgressBar() {
        JFrame frame = new JFrame("Performing analysis");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 150));
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Loading, please wait"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        JProgressBar pbar = new JProgressBar();
        pbar.setIndeterminate(true);
        pbar.setVisible(true);
        panel.add(pbar);
        frame.add(panel, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }
}
