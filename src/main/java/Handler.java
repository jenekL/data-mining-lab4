/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Mike
 */
public class Handler {

    private double ta;
    private double tb;
    private int sizeV;
    private double ya = 10000;
    private double yb = -1000;
    private double[] X;
    private double[] Y;
    private int N;
    private NET net;
    private double[][] pointsEd;
    private double[][] pointsTest;
    private double eEd = 0;
    private double eTest = 0;

    public Handler(int size, int sizeV, double ta, double tb) {
        N = size;
        this.sizeV = sizeV;
        this.ta = ta;
        this.tb = tb;
        X = new double[N];
        Y = new double[N];
        pointsEd = new double[4][];
        pointsTest = new double[4][];
        createXY();
    }

    public void createNET(int countHiddenLayer, int countNInHidden,
                          int countNInOut, double a, double E, int epoch,
                          IFunction hiddenF, IFunction outF) {
        net = new NET(sizeV, countHiddenLayer, countNInHidden, countNInOut, a, E, epoch, hiddenF, outF);
    }

    public void study() {
        int size = 3 * N / 4;
        double[][] xValue = new double[sizeV][size - sizeV];
        double[] yValue = new double[size - sizeV];

        double yy = (yb - ya);
        int index = 0;
        for (int i = 0; i < sizeV; i++) {
            index = 0;
            for (int j = i; j < size - sizeV + i; j++) {
                xValue[i][index] = (Y[j] - ya) / yy;
                index++;
            }
        }
        index = 0;
        for (int i = sizeV; i < size; i++) {
            yValue[index] = (Y[i] - ya) / yy;
            index++;
        }

        net.study(xValue, yValue);
        double[] ansY = new double[size - sizeV];
        for (int i = 0; i < size - sizeV; i++) {
            ansY[i] = Y[i + sizeV];
        }
        double[] ans = net.test(xValue);
        System.out.println("------Study-------");
        for (int i = 0; i < ans.length; i++) {
            double answer = ans[i] * yy + ya;
            System.out.println("x= " + answer + " y= " + ansY[i]);
        }
    }

    private void createXY() {
        double step = (tb - ta) / N;
        double noise = (tb - ta) * 0.2;
        for (int i = 0; i < N; i++) {
            X[i] = ta + step * i + noise;
            Y[i] = Math.sin(2 * X[i]) * X[i];
            if (Y[i] > yb) {
                yb = Y[i];
            }
            if (Y[i] < ya) {
                ya = Y[i];
            }
        }
    }

    public double[][] getPointsToGraph() {

        int c = N / 4 - sizeV;
        for (int i = 0; i < 4; i++) {
            pointsEd[i] = new double[c];
        }
        int index = 0;
        int begin = 3 * N / 4;
        double[][] xxValue = new double[sizeV][c];
        double yy = (yb - ya);
        for (int i = 0; i < sizeV; i++) {
            index = 0;
            for (int j = begin + i; j < N - sizeV + i; j++) {
                xxValue[i][index] = (Y[j] - ya) / yy;
                index++;
            }
        }
        index = 0;
        for (int i = begin + sizeV; i < N; i++) {
            pointsEd[0][index] = X[i];
            pointsEd[1][index] = Y[i];
            pointsEd[2][index] = X[i];
            index++;
        }
        eTest = 0;
        double[] ans = net.test(xxValue);

        for (int i = 0; i < c; i++) {
            pointsEd[3][i] = ans[i] * yy + ya;
            double err = Y[begin + sizeV + i] - pointsEd[3][i];
            err *= err;
            eTest += err;
        }
        eTest = eTest / c;
        eTest = Math.sqrt(eTest);
        return pointsEd;

    }

    public double[][] getEducationPointsToGraph() {

        int c = 3 * N / 4 - sizeV;
        //int c = N - sizeV;
        for (int i = 0; i < 4; i++) {
            pointsEd[i] = new double[c];
        }


        double[][] xxValue = new double[sizeV][c];
        double yy = (yb - ya);
        int index = 0;
        for (int i = 0; i < sizeV; i++) {
            index = 0;
            for (int j = i; j < c + i; j++) {
                xxValue[i][index] = (Y[j] - ya) / yy;
                index++;
            }
        }
        index = 0;
        for (int i = sizeV; i < c + sizeV; i++) {
            pointsEd[0][index] = X[i];
            pointsEd[1][index] = Y[i];
            pointsEd[2][index] = X[i];
            index++;
        }
        eEd = 0;
        double[] ans = net.test(xxValue);
        for (int i = 0; i < c; i++) {
            pointsEd[3][i] = ans[i] * yy + ya;
            double err = Y[i + sizeV] - pointsEd[3][i];
            err *= err;
            eEd += err;
        }
        eEd = eEd / c;
        eEd = Math.sqrt(eEd);
        return pointsEd;
    }

    /**
     * @return the eEd
     */
    public double geteEd() {
        return eEd;
    }

    /**
     * @return the eTest
     */
    public double geteTest() {
        return eTest;
    }
}
