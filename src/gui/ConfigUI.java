package gui;

import com.intellij.ui.components.JBScrollPane;
import config.TestSmellMetricsThresholdsList;
import storage.ConfigFileManager;
import config.TestSmellMetricThresholds;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

public class ConfigUI extends JFrame {
    private HashMap<TestSmellMetricThresholds, Map.Entry<JTextField, JTextField>> fields;
    private TestSmellMetricsThresholdsList metrics;
    private ArrayList<TestSmellMetricThresholds> metricsList;
    private JButton save;
    private JButton restore;
    private JButton exit;
    private File default_conf;
    private File conf;
    private String projdir;
    private JPanel gridPanel;
    // private SmellsThresholds thresholds;


    public ConfigUI(String projdir) throws HeadlessException {
        gridPanel = new JPanel();
        fields = new HashMap<>();
        this.projdir = projdir;
        this.setTitle("Metrics Thresholds Configuration");
        File default_conf = new File(System.getProperty("user.home") + "\\.temevi" + "\\default_config.ini");
        File conf = new File(System.getProperty("user.home") + "\\.temevi" + "\\config.ini");
       /*if(!default_conf.exists()) {
            thresholds = new SmellsThresholds(1,1,1,1,1,1,1,1,1);
            new ConfigFileHandler().writeThresholds(new File(projdir + "\\default_config.ini"), thresholds);
        }*/
        if (conf.exists())
            metrics = new ConfigFileManager().readThresholds(conf);
        else
            metrics = new ConfigFileManager().readThresholds(default_conf);

        metricsList = metrics.getThresholds();


        for (TestSmellMetricThresholds metric : metricsList) {
            JTextField detectionThresholdField = new JTextField(4);
            detectionThresholdField.setText("" + metric.getDetectionThreshold());
            JTextField guardThresholdField = new JTextField(4);
            guardThresholdField.setText("" + metric.getGuardThreshold());
            fields.put(metric, new AbstractMap.SimpleEntry<>(detectionThresholdField, guardThresholdField));
        }

        // System.out.println(arThresholds.get(0).getText());
        save = new JButton("Save");
        restore = new JButton("Restore default");
        exit = new JButton("Exit");
        setPreferredSize(new Dimension(800, 600));
        add(createPanel(), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }


    public JScrollPane createPanel() {
        Border margin = new EmptyBorder(10, 40, 10, 10);
        JPanel extPanel = new JPanel();
        extPanel.setBorder(margin);
        extPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        int size = metricsList.size();
        gridPanel.setLayout(new GridLayout((size * 5), 1));
        JScrollPane scrollPane = new JBScrollPane(extPanel);
        for (TestSmellMetricThresholds metrics : metricsList) {
            Map.Entry<JTextField, JTextField> entry = fields.get(metrics);
            JTextField detection = entry.getKey();
            JTextField guard = entry.getValue();
            addMetricPanel(metrics, detection, guard);
            gridPanel.add(new MyLine());
        }


        extPanel.add(gridPanel);
        //    scrollPane.add(arPanel);
        //  panel.add(scrollPane);

        return scrollPane;

    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel();
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    for (TestSmellMetricThresholds thresholds : metricsList) {
                        Map.Entry<JTextField, JTextField> entry = fields.get(thresholds);
                        JTextField detection = entry.getKey();
                        JTextField guard = entry.getValue();
                        double det = Double.parseDouble(detection.getText());
                        double gua = Double.parseDouble(guard.getText());

                        thresholds.setDetectionThreshold(det);
                        thresholds.setGuardThreshold(gua);
                    }

                    new ConfigFileManager().writeThresholds(new File(projdir + "\\config.ini"), metricsList);
                    JOptionPane.showMessageDialog(null, "NUOVE SOGLIE SALVATE CON SUCCESSO!");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(panel, "IL FORMATO DELLA SOGLIA NON RISPETTA IL TIPO DOUBLE!");
                }
            }
        });
        panel.add(save);
        restore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    FileUtils.forceDelete(conf);
                    ConfigUI.this.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        panel.add(restore);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
            }
        });
        panel.add(exit);
        return panel;
    }

    private JPanel valuePanel(JTextField field, String label) {
        JPanel valuePanel = new JPanel(new FlowLayout());
        //  System.out.println(field.getText());
        //    valuePanel.add(new JLabel("" + nome+ ":  "));
        valuePanel.add(new JLabel("<html>" + label + ":&nbsp;</html>"));
        valuePanel.add(field);
        return valuePanel;
    }

    private JPanel titlePanel(String title) {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Border border=BorderFactory.createLineBorder(Color.WHITE,1);
        JLabel label = new JLabel(title);
        label.setForeground(Color.YELLOW);

        titlePanel.add(label);
        return titlePanel;
    }

    private JPanel metricName(String name) {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Border border=BorderFactory.createLineBorder(Color.WHITE,1);
        JLabel label = new JLabel("Name: " + name);
        titlePanel.add(label);
        return titlePanel;
    }

    private JPanel descrPanel(String description) {
        JPanel descr = new JPanel(new FlowLayout(FlowLayout.LEFT));

        descr.add(new JLabel("<html>Description: " + description + "</html>"));
        // descr.setForeground(Color.YELLOW);
        return descr;
    }

    private void addMetricPanel(TestSmellMetricThresholds metric, JTextField detection, JTextField guard) {
        gridPanel.add(titlePanel(metric.getId()));
        gridPanel.add(metricName(metric.getName()));
        JPanel setValues = new JPanel(new FlowLayout());
        setValues.add(valuePanel(detection, "Detection Threshold"));
        setValues.add(new JLabel());
        setValues.add(valuePanel(guard, "Guard Threshold"));
        gridPanel.add(setValues);
        String description = metric.getDescription();
        description += "<br>Belonging Smells: ";
        ArrayList<String> smellsStrings = new ArrayList<>();
        for (TestSmellMetricThresholds.TestSmells smell : metric.getBelongingSmells()) {
            smellsStrings.add(smell.name());
        }
        for (int i = 0; i < smellsStrings.size() - 1; i++) {
            description += smellsStrings.get(i) + ",";
        }
        description += smellsStrings.get(smellsStrings.size() - 1);
        gridPanel.add(descrPanel(description));

    }
}



