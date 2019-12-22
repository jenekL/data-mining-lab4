import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class ScatterPlotExample extends JFrame {

    private XYPlot plot;

    public ScatterPlotExample(String title, XYDataset dataset) {
        super(title);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "KMeans",
                "X-Axis", "Y-Axis", dataset, PlotOrientation.VERTICAL,
                true, true, true);


        plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(255, 255, 255));

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);


        int sizeNeuronsPerLayout = 10;
        double alpha = 10;
        int numOfLayoutsHidden = 1;
        int numOfNeuronsPerLayoutHidden = 50;
        int numOfNeuronsInOut = 1;
        double speedStudying = 0.009;
        double accuracy = 0.0001;
        int numOfEpoch = 100;

        Handler handler = new Handler(sizeNeuronsPerLayout, plot);

        handler.createNET(numOfLayoutsHidden, numOfNeuronsPerLayoutHidden,
                numOfNeuronsInOut, speedStudying,
                accuracy, numOfEpoch,
                new SigmoidFunction(alpha), new LinearFunction());

        //new MainFrame(handler);

        handler.study();
    }

    public void setPanel(Container contentPane) {
        setContentPane(contentPane);
    }


    public static XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScatterPlotExample example = new ScatterPlotExample("iad4lab", ScatterPlotExample.createDataset());
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}