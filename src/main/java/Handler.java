/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Jenya
 */
public class Handler {

    private int K;
    private double ya = 10000;
    private double yb = -1000;
    private int N = 0;
    private NET net;

    private List<OHLC> ohlcList = new ArrayList<>();
    private int nTest;
    private int nStudy;

    XYPlot plot;

    public Handler(int sizeV, XYPlot plot) {
        this.K = sizeV;

        try {
            readFile("data.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        double[] k_study = getK(nStudy);
        double[] k_test = getK(nTest);

        XYSeries series = new XYSeries("K");
        for(int i = 0; i < k_study.length; i++){
            series.add(i + 1, k_study[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        savePNG(dataset, "k_study.png");

        series.clear();
        for(int i = 0; i < k_test.length; i++){
            series.add(i + 1, k_test[i]);
        }
        XYSeriesCollection dataset1 = new XYSeriesCollection(series);
        savePNG(dataset1, "k_test.png");

        System.out.println(Arrays.toString(k_study));
        System.out.println(Arrays.toString(k_test));

        this.plot = plot;
    }

    private void readFile(String filename) throws FileNotFoundException {
        N = 0;

        Scanner myReader = new Scanner(new File(filename));
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if (!data.contains("<")) {
                String[] split = data.split(",");
                ohlcList.add(new OHLC(split[2], split[3], Double.parseDouble(split[4]), Double.parseDouble(split[5]),
                        Double.parseDouble(split[6]), Double.parseDouble(split[7]), Double.parseDouble(split[8])));
                N++;
            }
        }

        nTest = N / 4;
        nStudy = N - nTest;

        ya = ohlcList.get(0).getOpen();
        yb = ohlcList.get(N - 1).getOpen();

        myReader.close();
    }

    private double[] getK(int n) {
        double[] k = new double[n];
        int i_k = 0;
        double sr = 0;
        for (int i = 0; i < nStudy; i++, i_k++) {
            sr += ohlcList.get(i).getOpen();
        }
        sr /= nStudy;

        double chisl = 0;
        double znam = 0;

        for (int k_i = 0; k_i < n; k_i++) {
            for (int i = k_i + 1; i < n; i++) {
                chisl += ((ohlcList.get(i).getOpen() - sr) * (ohlcList.get(i - k_i).getOpen() - sr));
                znam += (Math.pow(ohlcList.get(i).getOpen() - sr, 2));
            }
            k[k_i] = chisl / znam;
        }
        return k;
    }

    public void createNET(int countHiddenLayer, int countNInHidden,
                          int countNInOut, double speed, double E, int epoch,
                          IFunction hiddenF, IFunction outF) {
        net = new NET(K, countHiddenLayer, countNInHidden, countNInOut, speed, E, epoch, hiddenF, outF);
    }

    public void study() {
        XYSeries seriesTest = new XYSeries("Test");
        XYSeries seriesTestT = new XYSeries("TestT");
        XYSeries seriesStudy = new XYSeries("Study");
        XYSeries seriesStudyT = new XYSeries("StudyT");
        XYSeries seriesErrorStudy = new XYSeries("ErrorStudy");
        XYSeries seriesErrorTest = new XYSeries("ErrorTest");
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeriesCollection datasetError = new XYSeriesCollection();

        double[][] xValue = new double[K][nStudy];
        double[] yValue = new double[nStudy];

        double yy = (yb - ya);
        int index;
        for (int i = 0; i < K; i++) {
            index = 0;
            for (int j = i; j < nStudy + i; j++) {
                xValue[i][index] = (ohlcList.get(j).getOpen() - ya) / yy;
                index++;
            }
        }
        index = 0;
        for (int i = K; i < nStudy - 1; i++) {
            yValue[index] = (ohlcList.get(i).getOpen() - ya) / yy;
            index++;
        }

        net.study(xValue, yValue);
        double[] ansY = new double[nStudy];
        for (int i = 0; i < nStudy; i++) {
            ansY[i] = ohlcList.get(i + K).getOpen();
        }

        double[] ans = net.test(xValue);

        System.out.println("------Study-------");
        for (int i = 0; i < ans.length; i++) {
            double answer = ans[i] * yy + ya;
            System.out.println("guess= " + answer + " real= " + ansY[i]);
            seriesErrorStudy.add(i, Math.abs(ansY[i] - answer));
            seriesStudy.add(i, answer);
            seriesStudyT.add(i, ansY[i]);
        }

        System.out.println("------Test-------");
        xValue = new double[K][nTest];
        for (int i = 0; i < K; i++) {
            index = 0;
            for (int j = nStudy; j < N; j++) {
                xValue[i][index] = (ohlcList.get(j).getOpen() - ya) / yy;
                index++;
            }
        }

        ansY = new double[nTest];

        for (int i = 0; i < nTest; i++) {
            ansY[i] = ohlcList.get(i + nStudy).getOpen();
        }
        ans = net.test(xValue);
        for (int i = 0; i < ans.length; i++) {
            double answer = ans[i] * yy + ya;
            System.out.println("guess= " + answer + " real= " + ansY[i]);
            seriesErrorTest.add(i, Math.abs(ansY[i] - answer));
            seriesTest.add(i + nStudy, answer);
            seriesTestT.add(i + nStudy, ansY[i]);
        }

        dataset.addSeries(seriesTest);
        dataset.addSeries(seriesTestT);
        dataset.addSeries(seriesStudy);
        dataset.addSeries(seriesStudyT);

        datasetError.addSeries(seriesErrorStudy);
        datasetError.addSeries(seriesErrorTest);

        plot.setDataset(dataset);

        savePNG(datasetError, "error.png");
    }

    private void savePNG(XYSeriesCollection datasetError, String filename) {
        JFreeChart chart1 = ChartFactory.createXYLineChart(
                "ERROR",
                "X-Axis", "Y-Axis", datasetError, PlotOrientation.VERTICAL,
                true, true, true);

        XYPlot errorPlot = (XYPlot) chart1.getPlot();
        errorPlot.setDataset(datasetError);
        errorPlot = (XYPlot) chart1.getPlot();
        errorPlot.setBackgroundPaint(new Color(255, 255, 255));
        try {
            OutputStream out = new FileOutputStream(filename);
            ChartUtilities.writeChartAsPNG(out,
                    chart1,
                    1920,
                    1080);

        } catch (IOException ignored) {

        }
    }
}
