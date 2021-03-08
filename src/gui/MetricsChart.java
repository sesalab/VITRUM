package gui;

import com.intellij.openapi.diagnostic.Logger;
import config.TestSmellMetricThresholds;
import config.TestSmellMetricsThresholdsList;
import it.unisa.testSmellDiffusion.testSmellRules.TestSmellMetric;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;
import storage.AnalysisHistoryManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class MetricsChart {
    private static final Logger LOGGER = Logger.getInstance("global");

    private XYChart chart;
    private Vector<TestSmellMetric> metrics;
    private XChartPanel<XYChart> panel;
    private Vector<TestSmellMetricThresholds> thresholds;
    private String className;
    private String path;
    private int year;
    private int month;

    public MetricsChart(Vector<TestSmellMetric> metrics, Vector<TestSmellMetricThresholds> thresholds, String className, String path, int month, int year){
        this.metrics=metrics;
        this.thresholds=thresholds;
        this.className = className;
        this.path = path;
        this.year=year;
        this.month=month;

        panel = new XChartPanel<XYChart>(getChart());

    }


    public XYChart getChart() {
         chart = new XYChart(600,400);
        chart.setTitle("Metrics Evolution");
        chart.setXAxisTitle("Time");
        chart.setYAxisTitle("Metric value");
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setXAxisTicksVisible(false);
        double execs = 1;
        String realId;
        TestSmellMetric metricz = metrics.get(0);
        if(metricz.getId().equalsIgnoreCase("ar1")){
            realId="NONDA";
        }
        else if(metricz.getId().equalsIgnoreCase("et1")){
            realId="APCMC";
        }
        else if(metricz.getId().equalsIgnoreCase("gf1")){
            realId="GFMR";
        }
        else if(metricz.getId().equalsIgnoreCase("se1")){
            realId="TSEC";
        }
        else if(metricz.getId().equalsIgnoreCase("it1")){
            realId="MTOOR";
        }
        else if(metricz.getId().equalsIgnoreCase("ro1")){
            realId="NEXEA";
        }
        else if(metricz.getId().equalsIgnoreCase("mg1")){
            realId="MEXR";
        }
        else realId="Error";

        for(TestSmellMetric metric : metrics) {
            ArrayList<Double> xData = new ArrayList<>();
            ArrayList<Double> storic = new AnalysisHistoryManager().getStoricValues(className, metric.getId(), path + "\\reports", month, year);
            if (storic != null) {
                metric.setStoricValues(storic);
                execs = storic.size();

            for (double i = 0; i <= execs; i++) {
                xData.add(i);
            }


            chart.getStyler().setXAxisMax(execs);
            ArrayList<Double> yData = new ArrayList<>();
            yData.add(0.0);
                for (Double value : storic)
                    yData.add(value);

                chart.addSeries(realId, xData, yData);


              //  yData.add(metric.getValue());
               // chart.addSeries(metric.getId(), xData, yData);
            }
            if(storic == null){
                chart.setTitle("NO REPORTS FOUND");
            }
            TestSmellMetricThresholds threshold = TestSmellMetricsThresholdsList.getThresholdsById(realId, thresholds);
            double detectionThreshold = threshold.getDetectionThreshold();
            double criticThreshold = threshold.getGuardThreshold();
            XYSeries series = chart.addSeries("Threshold " + realId, new double[] {0.0, execs}, new double[]{detectionThreshold, detectionThreshold} );
            series.setLineColor(Color.YELLOW);
            series.setLineStyle(SeriesLines.DASH_DASH);
            XYSeries seriesCritic = chart.addSeries("Guard Threshold " + realId, new double[] {0.0, execs}, new double[]{criticThreshold, criticThreshold} );
            seriesCritic.setLineColor(Color.RED);
            seriesCritic.setLineStyle(SeriesLines.DASH_DASH);

        }


        return chart;
    }

    public void setChart(XYChart chart) {
        this.chart = chart;
    }


    public XChartPanel<XYChart> getPanel() {
        return panel;
    }

    public void setPanel(XChartPanel<XYChart> panel) {
        this.panel = panel;
    }
}
