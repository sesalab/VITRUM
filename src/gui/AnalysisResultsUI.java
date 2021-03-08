package gui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import it.unisa.testSmellDiffusion.beans.MethodBean;
import processor.MutationCoverageProcessor;
import storage.ConfigFileManager;
import config.TestSmellMetricThresholds;
import config.TestSmellMetricsThresholdsList;
import data.TestClassAnalysis;
import data.TestProjectAnalysis;
import it.unisa.testSmellDiffusion.testSmellRules.TestSmellMetric;
import storage.AnalysisHistoryManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormatSymbols;
import java.util.*;

public class AnalysisResultsUI extends JFrame {
    private static final Logger LOGGER = Logger.getInstance("global");

    private TestProjectAnalysis project;
    private JPanel eastPanel;
    private Vector<TestSmellMetric> selectedMetrics;
    private MetricsChart chart;
    private TestSmellMetricsThresholdsList list;
    private ArrayList<TestSmellMetricThresholds> thresholdsList;
    private Vector<TestSmellMetricThresholds> selectedThresholds;
    private Vector<TestClassAnalysis> classAnalysis;
    private JPanel mainPanel;
    private JPanel classPanel;
    private JList<TestClassAnalysis> classList;
    private int selectedSmell;
    private int prevMonth = 1;

    public AnalysisResultsUI(TestProjectAnalysis project) throws HeadlessException {
        this.project = project;
        this.selectedSmell = 0;
        File default_conf = new File(System.getProperty("user.home") + "\\.temevi" + "\\default_config.ini");
        File conf = new File(System.getProperty("user.home") + "\\.temevi" + "\\config.ini");
       /*if(!default_conf.exists()) {
            thresholds = new SmellsThresholds(1,1,1,1,1,1,1,1,1);
            new ConfigFileHandler().writeThresholds(new File(projdir + "\\default_config.ini"), thresholds);
        }*/
        if (conf.exists())
            list = new ConfigFileManager().readThresholds(conf);
        else
            list = new ConfigFileManager().readThresholds(default_conf);

        if (list == null) throw new NullPointerException();
        setPreferredSize(new Dimension(1440, 820));
        setTitle("Analysis Results");
        add(projectPanel(), BorderLayout.NORTH);
        classPanel = classPanel();
        add(classPanel, BorderLayout.WEST);
        // grid.add(classPanel());
        // grid.add(new JLabel());
        eastPanel = new JPanel();
        eastPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        //grid.add(eastPanel);
        add(eastPanel, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel classPanel() {
        try {
            JPanel west = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel panel = new JPanel();
            west.setBorder(new EmptyBorder(15, 15, 15, 15));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            comboPanel.add(new JLabel("Filter by smell: "));
            comboPanel.add(smellFilterPanel());
            panel.add(comboPanel);
            JLabel titleText = new JLabel(("Classes list"));
            titleText.setFont(titleText.getFont().deriveFont(15.0f));
            JPanel title = new JPanel(new FlowLayout(FlowLayout.LEFT));
            title.add(titleText);
            panel.add(title);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));

            JLabel yellow = null;
            JLabel red = null;
            classAnalysis = project.getClassAffectedBySmell(selectedSmell);
            switch (selectedSmell) {
                case 0:
                    classAnalysis.sort(new Comparator<TestClassAnalysis>() {
                        @Override
                        public int compare(TestClassAnalysis o1, TestClassAnalysis o2) {
                            return o1.getSmells().getWeight() > o2.getSmells().getWeight() ? -1 : (o1.getSmells().getWeight() < o2.getSmells().getWeight() ? 1 : 0);
                        }
                    });
                    yellow = new JLabel("*Class is affected by one or more smells");
                    yellow.setForeground(Color.YELLOW);
                    red = new JLabel(("**Class is critically affected by one or more smells"));
                    red.setForeground(new Color(255, 102, 102));

                    break;
                case 1:
                    classAnalysis.sort(new Comparator<TestClassAnalysis>() {
                        @Override
                        public int compare(TestClassAnalysis o1, TestClassAnalysis o2) {
                            return o1.getSmells().getAssertionRoulette() > o2.getSmells().getAssertionRoulette() ? -1 : (o1.getSmells().getAssertionRoulette() < o2.getSmells().getAssertionRoulette() ? 1 : 0);

                        }
                    });
                    yellow = new JLabel("*Class is affected by Assertion Roulette smell");
                    yellow.setForeground(Color.YELLOW);
                    red = new JLabel(("**Class is critically affected by Assertion Roulette smell"));
                    red.setForeground(new Color(255, 102, 102));
                    break;
                case 2:
                    classAnalysis.sort(new Comparator<TestClassAnalysis>() {
                        @Override
                        public int compare(TestClassAnalysis o1, TestClassAnalysis o2) {
                            return o1.getSmells().getEagerTest() > o2.getSmells().getEagerTest() ? -1 : (o1.getSmells().getEagerTest() < o2.getSmells().getEagerTest() ? 1 : 0);

                        }
                    });
                    yellow = new JLabel("*Class is affected by Eager Test smell");
                    yellow.setForeground(Color.YELLOW);
                    red = new JLabel(("**Class is critically affected by Eager Test smell"));
                    red.setForeground(new Color(255, 102, 102));
                    break;
                case 3:
                    classAnalysis.sort(new Comparator<TestClassAnalysis>() {
                        @Override
                        public int compare(TestClassAnalysis o1, TestClassAnalysis o2) {
                            return o1.getSmells().getGeneralFixture() > o2.getSmells().getGeneralFixture() ? -1 : (o1.getSmells().getGeneralFixture() < o2.getSmells().getGeneralFixture() ? 1 : 0);

                        }
                    });
                    yellow = new JLabel("*Class is affected by General Fixture smell");
                    yellow.setForeground(Color.YELLOW);
                    red = new JLabel(("**Class is critically affected by General Fixture smell"));
                    red.setForeground(new Color(255, 102, 102));
                    break;
                case 4:
                    classAnalysis.sort(new Comparator<TestClassAnalysis>() {
                        @Override
                        public int compare(TestClassAnalysis o1, TestClassAnalysis o2) {
                            return o1.getSmells().getSensitiveEquality() > o2.getSmells().getSensitiveEquality() ? -1 : (o1.getSmells().getSensitiveEquality() < o2.getSmells().getSensitiveEquality() ? 1 : 0);

                        }
                    });
                    yellow = new JLabel("*Class is affected by Sensitive Equality smell");
                    yellow.setForeground(Color.YELLOW);
                    red = new JLabel(("**Class is critically affected by Sensitive Equality smell"));
                    red.setForeground(new Color(255, 102, 102));
                    break;
                case 5:
                    classAnalysis.sort(new Comparator<TestClassAnalysis>() {
                        @Override
                        public int compare(TestClassAnalysis o1, TestClassAnalysis o2) {
                            return o1.getSmells().getMysteryGuest() > o2.getSmells().getMysteryGuest() ? -1 : (o1.getSmells().getMysteryGuest() < o2.getSmells().getMysteryGuest() ? 1 : 0);

                        }
                    });
                    yellow = new JLabel("*Class is affected by Mystery Guest smell");
                    yellow.setForeground(Color.YELLOW);
                    red = new JLabel(("**Class is critically affected by Mystery Guest smell"));
                    red.setForeground(new Color(255, 102, 102));
                    break;
                case 6:
                    classAnalysis.sort(new Comparator<TestClassAnalysis>() {
                        @Override
                        public int compare(TestClassAnalysis o1, TestClassAnalysis o2) {
                            return o1.getSmells().getIndirectTesting() > o2.getSmells().getIndirectTesting() ? -1 : (o1.getSmells().getIndirectTesting() < o2.getSmells().getIndirectTesting() ? 1 : 0);

                        }
                    });
                    yellow = new JLabel("*Class is affected by Indirect Testing smell");
                    yellow.setForeground(Color.YELLOW);
                    red = new JLabel(("**Class is critically affected by Indirect Testing smell"));
                    red.setForeground(new Color(255, 102, 102));
                    break;
                case 7:
                    classAnalysis.sort(new Comparator<TestClassAnalysis>() {
                        @Override
                        public int compare(TestClassAnalysis o1, TestClassAnalysis o2) {
                            return o1.getSmells().getResourceOptimism() > o2.getSmells().getResourceOptimism() ? -1 : (o1.getSmells().getResourceOptimism() < o2.getSmells().getResourceOptimism() ? 1 : 0);

                        }
                    });
                    yellow = new JLabel("*Class is affected by Resource Optimism smell");
                    yellow.setForeground(Color.YELLOW);
                    red = new JLabel(("**Class is critically affected by Resource Optimism smell"));
                    red.setForeground(new Color(255, 102, 102));
                    break;

            }


            classList = classList();
            classList.addListSelectionListener(new ClassListListener());

            JScrollPane scrollPane = new JBScrollPane(classList);
            scrollPane.setPreferredSize(new Dimension(100, 500));
            panel.add(scrollPane);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            JPanel text = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel lol = new JPanel();
            lol.setLayout(new BoxLayout(lol, BoxLayout.Y_AXIS));
            lol.add(yellow);
            lol.add(red);
            text.add(lol);
            panel.add(text);
            west.add(panel);

            return west;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return null;
        }
    }

    private JPanel projectPanel() {
        JPanel projectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        projectPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Project Name: " + project.getName()));
        panel.add(new JLabel());
        panel.add(new JLabel("Nr. of test classes: " + project.getTestClassesNumber()));
        panel.add(new JLabel("Nr. of test classes affected by smells: " + project.getAffectedClasses()));
        panel.add(new JLabel("LOC: " + project.getLoc() + "   NOM: " + project.getNom() + "   Line Coverage: " + project.getLineCoverage() + "   Branch Coverage: " + project.getBranchCoverage() + "         "));
        JButton buttonCov = new JButton();
        buttonCov.setText("Detailed Coverage Report");
        buttonCov.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File htmlFile = new File(System.getProperty("user.home") + "\\.temevi\\htmlCoverage\\index.html");
                    Desktop.getDesktop().browse(htmlFile.toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(buttonCov);
        projectPanel.add(panel);
        return projectPanel;

    }

    private JComboBox<String> smellFilterPanel() {
        JComboBox<String> smellFilter = new ComboBox<>();
        smellFilter.addItem("All");
        smellFilter.addItem("Assertion Roulette");
        smellFilter.addItem("Eager Test");
        smellFilter.addItem("General Fixture");
        smellFilter.addItem("Sensitive Equality");
        smellFilter.addItem("Mystery Guest");
        smellFilter.addItem("Indirect Testing");
        smellFilter.addItem("Resource Optimism");
        smellFilter.setSelectedIndex(selectedSmell);
        // smellFilter.setMaximumSize(new Dimension(120,6));
        smellFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedSmell = smellFilter.getSelectedIndex();
                BorderLayout layout = (BorderLayout) AnalysisResultsUI.this.getLayout();
                AnalysisResultsUI.this.eastPanel.removeAll();
                AnalysisResultsUI.this.classPanel.removeAll();
                AnalysisResultsUI.this.remove(classPanel);
                AnalysisResultsUI.this.classPanel = classPanel();
                AnalysisResultsUI.this.add(classPanel, BorderLayout.WEST);
                AnalysisResultsUI.this.revalidate();
                AnalysisResultsUI.this.repaint();

            }
        });
        return smellFilter;
    }

    private JList<TestClassAnalysis> classList() {
        JList<TestClassAnalysis> classPanel = new JBList<>(classAnalysis);
        classPanel.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof TestClassAnalysis) {
                    // Here value will be of the Type 'CD'
                    ((JLabel) renderer).setText(((TestClassAnalysis) value).getName());
                    TestClassAnalysis colorChange = (TestClassAnalysis) value;
                    JLabel text = (JLabel) renderer;
                    renderer.setFont((text.getFont().deriveFont(15.0f)));
                    switch (selectedSmell) {
                        case 0:
                            if (colorChange.getSmells().isAffectedCritic()) {
                                renderer.setForeground(new Color(255, 102, 102));
                            } else if (colorChange.getSmells().isAffected())
                                renderer.setForeground(Color.YELLOW);
                            break;
                        case 1:
                            renderer.setForeground(Color.YELLOW);
                            if (colorChange.getSmells().getAssertionRoulette() == 2)
                                renderer.setForeground(new Color(255, 102, 102));
                            break;
                        case 2:
                            renderer.setForeground(Color.YELLOW);
                            if (colorChange.getSmells().getEagerTest() == 2)
                                renderer.setForeground(new Color(255, 102, 102));
                            break;
                        case 3:
                            renderer.setForeground(Color.YELLOW);
                            if (colorChange.getSmells().getGeneralFixture() == 2)
                                renderer.setForeground(new Color(255, 102, 102));
                            break;

                        case 4:
                            renderer.setForeground(Color.YELLOW);
                            if (colorChange.getSmells().getSensitiveEquality() == 2)
                                renderer.setForeground(new Color(255, 102, 102));
                            break;
                        case 5:
                            renderer.setForeground(Color.YELLOW);
                            if (colorChange.getSmells().getMysteryGuest() == 2)
                                renderer.setForeground(new Color(255, 102, 102));
                            break;
                        case 6:
                            renderer.setForeground(Color.YELLOW);
                            if (colorChange.getSmells().getIndirectTesting() == 2)
                                renderer.setForeground(new Color(255, 102, 102));
                            break;
                        case 7:
                            renderer.setForeground(Color.YELLOW);
                            if (colorChange.getSmells().getResourceOptimism() == 2)
                                renderer.setForeground(new Color(255, 102, 102));
                            break;

                    }

                }
                return renderer;
            }
        });
        classPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return classPanel;
    }

    private JPanel filterPanel(TestClassAnalysis selected, Vector<TestSmellMetric> selectedMetrics, Vector<TestSmellMetricThresholds> selectedThresholds) {
        GregorianCalendar calendar = new GregorianCalendar();
        JSlider yearSlider = new JSlider(JSlider.HORIZONTAL, calendar.get(Calendar.YEAR) - 3, calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) - 3);
        yearSlider.setMajorTickSpacing(1);
        yearSlider.setMinorTickSpacing(1);
        yearSlider.setPaintTicks(true);
        yearSlider.setPaintLabels(true);
        int currMonth = calendar.get(Calendar.MONTH) + 1;
        int currYear = calendar.get(Calendar.YEAR);
        int max = 12;


        JSlider monthSlider = new JSlider(JSlider.HORIZONTAL, 1, 12, 1);
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String[] months = dfs.getShortMonths();
        Hashtable hashTable = new Hashtable(12);
        for (int i = 0; i < 12; i++)
            hashTable.put(i + 1, new JLabel(months[i],
                    JLabel.CENTER));
        monthSlider.setLabelTable(hashTable);
        monthSlider.setPaintLabels(true);
        monthSlider.setMajorTickSpacing(1);
        monthSlider.setPaintTicks(true);
        monthSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int month = monthSlider.getValue();
                int year = yearSlider.getValue();
                chart = new MetricsChart(selectedMetrics, selectedThresholds, selected.getBelongingPackage() + "." + selected.getName(), project.getPath(), month, year);
                mainPanel.remove(4);
                mainPanel.add(chart.getPanel());
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        yearSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int year = yearSlider.getValue();
                int month = monthSlider.getValue();
                chart = new MetricsChart(selectedMetrics, selectedThresholds, selected.getBelongingPackage() + "." + selected.getName(), project.getPath(), month, year);

                mainPanel.remove(4);
                mainPanel.add(chart.getPanel());
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Show smells metrics evolution starting from:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(monthSlider);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(yearSlider);

        return panel;
    }

    private class ClassListListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            GregorianCalendar calendar = new GregorianCalendar();

            if (!listSelectionEvent.getValueIsAdjusting()) {
                TestClassAnalysis selected = classList.getSelectedValue();
                if (selected != null) {
                    eastPanel.removeAll();
                    eastPanel.revalidate();
                    eastPanel.repaint();

                    JPanel azz = new JPanel();
                    azz.setLayout(new BoxLayout(azz, BoxLayout.X_AXIS));
                    JPanel classInfo = new JPanel();
                    classInfo.setLayout(new BoxLayout(classInfo, BoxLayout.Y_AXIS));
                    classInfo.add(new JLabel("<html>Belonging package:<font color='white'> " + selected.getBelongingPackage() + "</font></html>"));
                    classInfo.add(new JLabel("<html>Class name:<font color='white'> " + selected.getName() + "</font></html>"));
                    classInfo.add(new JLabel("<html>LOC:<font color='white'> " + selected.getCkMetrics().getLoc() + "</font></html>"));
                    classInfo.add(new JLabel("<html>NOM:<font color='white'> " + selected.getCkMetrics().getNom() + "</font></html>"));
                    classInfo.add(new JLabel("<html>WMC:<font color='white'> " + selected.getCkMetrics().getWmc() + "</font></html>"));
                    classInfo.add(new JLabel("<html>RFC: <font color='white'> " + selected.getCkMetrics().getRfc() + "</font></html>"));
                    classInfo.add(new JLabel("<html>Assertion Density: <font color='white'> " + selected.getCoverage().getAssertionDensity() + "</font></html>"));
                    if (selected.getCoverage().getLineCoverage() != -1.0d) {
                        double prevLineCov = new AnalysisHistoryManager().getPreviousLineCoverage(selected.getBelongingPackage() + "." + selected.getName(), project.getPath() + "\\reports");
                        if (prevLineCov != -1) {
                            String color;
                            if (selected.getCoverage().getLineCoverage() > prevLineCov) color = "#00e600";
                            else if (selected.getCoverage().getLineCoverage() < prevLineCov) color = "#ff6666";
                            else color = "'white'";
                            JLabel label = new JLabel("<html>Line Coverage: <font color=" + color + "> " + selected.getCoverage().getLineCoverage() + "</font>" + "&nbsp;&nbsp;(was " + prevLineCov + ")</html>");
                            classInfo.add(label);
                        } else
                            classInfo.add(new JLabel("<html>Line Coverage: <font color='white'> " + selected.getCoverage().getLineCoverage() + "</font></html>"));
                    } else {
                        classInfo.add(new JLabel("<html>Line Coverage: <font color='white'>N/A</font></html>"));

                    }

                    double prevBranchCov = new AnalysisHistoryManager().getPreviousBranchCoverage(selected.getBelongingPackage() + "." + selected.getName(), project.getPath() + "\\reports");
                    if (selected.getCoverage().getBranchCoverage() == -1.0d)
                        classInfo.add(new JLabel("<html>Branch Coverage: <font color='white'> N/A</font></html>"));

                    else {
                        if (prevBranchCov != -1.0) {
                            String color;
                            if (selected.getCoverage().getBranchCoverage() > prevBranchCov) color = "#00e600";
                            else if (selected.getCoverage().getBranchCoverage() < prevBranchCov)
                                color = "#ff6666";
                            else color = "'white'";
                            JLabel label = new JLabel("<html>Branch Coverage: <font color=" + color + "> " + selected.getCoverage().getBranchCoverage() + "</font>" + "&nbsp;&nbsp;(was " + prevBranchCov + ")</html>");
                            classInfo.add(label);
                        } else
                            classInfo.add(new JLabel("<html>Branch Coverage: <font color='white'> " + selected.getCoverage().getBranchCoverage() + "</font></html>"));

                    }
                    if (selected.getMutationCoverage().getMutationCoverage() >= 0.0d) {
                        classInfo.add(new JLabel("<html>Mutation Coverage: <font color='white'> " + selected.getMutationCoverage().getMutationCoverage() + "</font></html>"));
                        JButton button = new JButton("Detailed mutation testing report");
                        File report = new File(selected.getMutationCoverage().getReportName());
                        button.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Desktop.getDesktop().browse(report.toURI());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        classInfo.add(button);

                    } //else
                       // classInfo.add(new JLabel("<html>Mutation Coverage: <font color='white'>Mutation Testing interrupted. Increase the timeout.</font></html>"));

                    if (selected.getFlakyTests().getFlakyMethods() != null && selected.getFlakyTests().getFlakyMethods().size() == 0)
                        classInfo.add(new JLabel("<html>Flaky Tests: <font color='white'>0 tests detected</font></html>"));
                    else if (selected.getFlakyTests().getFlakyMethods() != null && selected.getFlakyTests().getFlakyMethods().size() > 0) {
                        classInfo.add(new JLabel("<html>Flaky Tests: <font color='white'>" + selected.getFlakyTests().getFlakyMethods().size() + " tests detected</font></html>"));
                        JButton flakyButton = new JButton();
                        flakyButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                ArrayList<String> flaky = new ArrayList<>();
                                for (MethodBean method : selected.getFlakyTests().getFlakyMethods())
                                    flaky.add(method.getName());
                                JList<String> list = new JBList<>(flaky);
                                JScrollPane scroll = new JBScrollPane(list);
                                JFrame flakyFrame = new JFrame();
                                flakyFrame.setTitle("Flaky Tests");
                                flakyFrame.setLocationRelativeTo(null);
                                flakyFrame.setSize(new Dimension(200, 300));
                                flakyFrame.add(scroll);
                                flakyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                flakyFrame.setVisible(true);
                            }
                        });
                        flakyButton.setText("Show Flaky Methods");
                        classInfo.add(flakyButton);

                    }

                    String affected = "";
                    if (selected.getSmells().getAssertionRoulette() == 1 || selected.getSmells().getAssertionRoulette() == 2)
                        affected += "- Assertion Roulette<br>";
                    if (selected.getSmells().getEagerTest() == 1 || selected.getSmells().getEagerTest() == 2)
                        affected += "- Eager Test<br>";
                    if (selected.getSmells().getGeneralFixture() == 1 || selected.getSmells().getGeneralFixture() == 2)
                        affected += "- General Fixture<br>";
                    if (selected.getSmells().getIndirectTesting() == 1 || selected.getSmells().getIndirectTesting() == 2)
                        affected += "- Indirect Testing<br>";
                    if (selected.getSmells().getMysteryGuest() == 1 || selected.getSmells().getMysteryGuest() == 2)
                        affected += "- Mystery Guest<br>";
                    if (selected.getSmells().getResourceOptimism() == 1 || selected.getSmells().getResourceOptimism() == 2)
                        affected += "- Resource Optimism<br>";
                    if (selected.getSmells().getSensitiveEquality() == 1 || selected.getSmells().getSensitiveEquality() == 2)
                        affected += "- Sensitive Equality";
                    classInfo.add(new JLabel("<html>Affected by these smells:<br> <font color='white'>" + affected + "</font></html>"));

                    classInfo.setBorder(new EmptyBorder(0, 0, 250, 40));
                    azz.add(classInfo);

                    JComboBox<String> smells = new ComboBox<>();
                    smells.addItem("Assertion Roulette");
                    smells.addItem("Eager Test");
                    smells.addItem("General Fixture");
                    smells.addItem("Sensitive Equality");
                    smells.addItem("Mystery Guest");
                    smells.addItem("Indirect Testing");
                    smells.addItem("Resource Optimism");

                    JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    comboPanel.add(new JLabel("Select a smell: "));
                    comboPanel.add(smells);
                    mainPanel = new JPanel();
                    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                    mainPanel.add(comboPanel);
                    mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                    if (selectedSmell != 0)
                        smells.setSelectedIndex(selectedSmell - 1);
                    else
                        smells.setSelectedIndex(selectedSmell);
                    String selectedSmell = (String) smells.getSelectedItem();
                    if (selectedSmell.equalsIgnoreCase("assertion roulette")) {
                        selectedMetrics = selected.getSmells().getMetrics().getArMetrics();
                        selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.ASSERTION_ROULETTE);
                    } else if (selectedSmell.equalsIgnoreCase("eager test")) {
                        selectedMetrics = selected.getSmells().getMetrics().getEtMetrics();
                        selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.EAGER_TEST);
                    } else if (selectedSmell.equalsIgnoreCase("general fixture")) {
                        selectedMetrics = selected.getSmells().getMetrics().getGfMetrics();
                        selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.GENERAL_FIXTURE);
                    } else if (selectedSmell.equalsIgnoreCase("mystery guest")) {
                        selectedMetrics = selected.getSmells().getMetrics().getMgMetrics();
                        selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.MYSTERY_GUEST);
                    } else if (selectedSmell.equalsIgnoreCase("sensitive equality")) {
                        selectedMetrics = selected.getSmells().getMetrics().getSeMetrics();
                        selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.SENSITIVE_EQUALITY);
                    } else if (selectedSmell.equalsIgnoreCase("resource optimism")) {
                        selectedMetrics = selected.getSmells().getMetrics().getRoMetrics();
                        selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.RESOURCE_OPTIMISM);
                    } else if (selectedSmell.equalsIgnoreCase("indirect testing")) {
                        selectedMetrics = selected.getSmells().getMetrics().getItMetrics();
                        selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.INDIRECT_TESTING);
                    }
                    smells.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            String selectedSmell = (String) smells.getSelectedItem();
                            if (selectedSmell.equalsIgnoreCase("assertion roulette")) {
                                selectedMetrics = selected.getSmells().getMetrics().getArMetrics();
                                selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.ASSERTION_ROULETTE);
                            } else if (selectedSmell.equalsIgnoreCase("eager test")) {
                                selectedMetrics = selected.getSmells().getMetrics().getEtMetrics();
                                selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.EAGER_TEST);
                            } else if (selectedSmell.equalsIgnoreCase("general fixture")) {
                                selectedMetrics = selected.getSmells().getMetrics().getGfMetrics();
                                selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.GENERAL_FIXTURE);
                            } else if (selectedSmell.equalsIgnoreCase("mystery guest")) {
                                selectedMetrics = selected.getSmells().getMetrics().getMgMetrics();
                                selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.MYSTERY_GUEST);
                            } else if (selectedSmell.equalsIgnoreCase("sensitive equality")) {
                                selectedMetrics = selected.getSmells().getMetrics().getSeMetrics();
                                selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.SENSITIVE_EQUALITY);
                            } else if (selectedSmell.equalsIgnoreCase("resource optimism")) {
                                selectedMetrics = selected.getSmells().getMetrics().getRoMetrics();
                                selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.RESOURCE_OPTIMISM);
                            } else if (selectedSmell.equalsIgnoreCase("indirect testing")) {
                                selectedMetrics = selected.getSmells().getMetrics().getItMetrics();
                                selectedThresholds = list.getThresholdsBySmell(TestSmellMetricThresholds.TestSmells.INDIRECT_TESTING);
                            }
                            chart = new MetricsChart(selectedMetrics, selectedThresholds, selected.getBelongingPackage() + "." + selected.getName(), project.getPath(), 1, calendar.get(Calendar.YEAR) - 3);
                            mainPanel.remove(4);
                            mainPanel.remove(3);
                            mainPanel.remove(2);

                            mainPanel.add(filterPanel(selected, selectedMetrics, selectedThresholds));
                            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

                            mainPanel.add(chart.getPanel());
                            mainPanel.revalidate();
                            mainPanel.repaint();

                        }
                    });
                    mainPanel.add(filterPanel(selected, selectedMetrics, selectedThresholds));
                    chart = new MetricsChart(selectedMetrics, selectedThresholds, selected.getBelongingPackage() + "." + selected.getName(), project.getPath(), 1, calendar.get(Calendar.YEAR) - 3);

                    mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                    mainPanel.add(chart.getPanel());
                    azz.add(mainPanel);
                    eastPanel.add(azz);
                }
            }
        }
    }
}
