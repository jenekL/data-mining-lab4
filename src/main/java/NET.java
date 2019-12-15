/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jenya
 */
public class NET {

    private Layer sensors;
    private Layer[] hiddenLayer;
    private Layer outterLayer;
    private IFunction activateHiddenFunction;
    private IFunction activateOutterFunction;
    private double a;
    private double Em;
    private int epoch;

    public NET(int cNeuronsInSensors, int cHiddenLayer, int cNeuronsInHiddenLayer,
               int cNeuronsInOutterLayer, double a, double Em,int epoch,
               IFunction hidden, IFunction out) {
        hiddenLayer = new Layer[cHiddenLayer];
        hiddenLayer[cHiddenLayer - 1] = new Layer(cNeuronsInHiddenLayer, cNeuronsInOutterLayer);
        for (int i = 0; i < cHiddenLayer - 1; i++) {
            hiddenLayer[i] = new Layer(cNeuronsInHiddenLayer, cNeuronsInHiddenLayer);
        }
        sensors = new Layer(cNeuronsInSensors, cNeuronsInHiddenLayer);
        outterLayer = new Layer(cNeuronsInOutterLayer, 0);
        this.a = a;
        this.Em = Em;
        this.activateHiddenFunction = hidden;
        this.activateOutterFunction = out;
        this.epoch = epoch;
    }

    public void study(double[][] inputValues, double[] answers) {
        double[][] err = new double[hiddenLayer.length][hiddenLayer[0].size()];
        double gError = 0;
        double[] y_in = new double[outterLayer.size()];
        double[][] z_in = new double[hiddenLayer.length][hiddenLayer[0].size()];
        int COUNT = 0;
        for (int t = 0; t < epoch; t++) {
            COUNT = 0;
            do {
            gError = 0;
            for (int j = 0; j < inputValues[0].length; j++) {
                for (int i = 0; i < inputValues.length; i++) {
                    sensors.getNeuron(i).setValue(inputValues[i][j]);
                }
                // от сенсора к 1му слою скрытому
                for (int i = 0; i < hiddenLayer[0].size(); i++) {
                    z_in[0][i] = 0;
                    for (int jj = 0; jj < sensors.size(); jj++) {
                        z_in[0][i] += sensors.getNeuron(jj).getValue() * sensors.getNeuron(jj).getWeight(i);
                    }
                    hiddenLayer[0].getNeuron(i).setValue(activateHiddenFunction.compute(z_in[0][i]));
                }
                // внутри скрытых слоёв
                for (int i = 1; i < hiddenLayer.length; i++) {
                    for (int l = 0; l < hiddenLayer[i].size(); l++) {
                        z_in[i][l] = 0;
                        for (int k = 0; k < hiddenLayer[i - 1].size(); k++) {
                            z_in[i][l] += hiddenLayer[i - 1].getNeuron(k).getValue() * hiddenLayer[i - 1].getNeuron(k).getWeight(l);
                        }
                        hiddenLayer[i].getNeuron(l).setValue(activateHiddenFunction.compute(z_in[i][l]));
                    }
                }
                // от последнего скрытого к выходному
                int lastIndex = hiddenLayer.length - 1;
                for (int i = 0; i < outterLayer.size(); i++) {
                    y_in[i] = 0;
                    for (int jj = 0; jj < hiddenLayer[lastIndex].size(); jj++) {
                        y_in[i] += hiddenLayer[lastIndex].getNeuron(jj).getValue() * hiddenLayer[lastIndex].getNeuron(jj).getWeight(i);
                    }
                    outterLayer.getNeuron(i).setValue(activateOutterFunction.compute(y_in[i]));
                }
                // обратное распространение ошибки
                double out = outterLayer.getNeuron(0).getValue();
                double lErr = (answers[j] - out) * activateOutterFunction.computeFirstDerivative(y_in[0]);
                gError += Math.abs(lErr);

                // ищем ошибку на последнем скрытом слое
                for (int i = 0; i < hiddenLayer[lastIndex].size(); i++) {
                    err[lastIndex][i] = lErr * hiddenLayer[lastIndex].getNeuron(i).getWeight(0) * activateHiddenFunction.computeFirstDerivative(z_in[lastIndex][i]);
                }
                // ищем ошубки на остальных скрытых слоях
                for (int i = lastIndex - 1; i >= 0; i--) {
                    for (int k = 0; k < hiddenLayer[i].size(); k++) {
                        err[i][k] = 0;
                        for (int l = 0; l < hiddenLayer[i + 1].size(); l++) {
                            err[i][k] += err[i + 1][l] * hiddenLayer[i].getNeuron(k).getWeight(l);
                        }
                        err[i][k] = err[i][k] * activateHiddenFunction.computeFirstDerivative(z_in[i][k]);
                    }
                }

                // меняем веса между последним скрытым слоем и выходом
                for (int l = 0; l < hiddenLayer[lastIndex].size(); l++) {
                    double d = a * lErr * hiddenLayer[lastIndex].getNeuron(l).getValue();
                    hiddenLayer[0].getNeuron(l).addWeight(0, d);
                }
                // меняем веса внутри скрытого слоя
                for (int i = lastIndex - 1; i >= 0; i--) {
                    for (int k = 0; k < hiddenLayer[i].size(); k++) {
                        for (int l = 0; l < hiddenLayer[i + 1].size(); l++) {
                            double d = a * err[i][l] * hiddenLayer[i].getNeuron(k).getValue();
                            hiddenLayer[i].getNeuron(k).addWeight(l, d);
                        }
                    }
                }
                // меняем веса между сенсорами и первым скрытым слоем
                for (int i = 0; i < sensors.size(); i++) {
                    for (int l = 0; l < hiddenLayer[0].size(); l++) {
                        double d = a * err[0][l] * sensors.getNeuron(i).getValue();
                        sensors.getNeuron(i).addWeight(l, d);

                    }
                }
                COUNT++;
            }
            gError = gError / inputValues[0].length;
            } while (gError > Em && COUNT < 1000);
        }

    }

    public void countOutter() {
        double value;
        for (int i = 0; i < hiddenLayer[0].size(); i++) {
            value = 0;
            for (int jj = 0; jj < sensors.size(); jj++) {
                value += sensors.getNeuron(jj).getValue() * sensors.getNeuron(jj).getWeight(i);
            }
            hiddenLayer[0].getNeuron(i).setValue(activateHiddenFunction.compute(value));
        }
        // внутри скрытых слоёв
        for (int i = 1; i < hiddenLayer.length; i++) {
            for (int l = 0; l < hiddenLayer[i].size(); l++) {
                value = 0;
                for (int k = 0; k < hiddenLayer[i - 1].size(); k++) {
                    value += hiddenLayer[i - 1].getNeuron(k).getValue() * hiddenLayer[i - 1].getNeuron(k).getWeight(l);
                }
                hiddenLayer[i].getNeuron(l).setValue(activateHiddenFunction.compute(value));
            }
        }
        // от последнего скрытого к выходному
        int lastIndex = hiddenLayer.length - 1;
        for (int i = 0; i < outterLayer.size(); i++) {
            value = 0;
            for (int jj = 0; jj < hiddenLayer[lastIndex].size(); jj++) {
                value += hiddenLayer[lastIndex].getNeuron(jj).getValue() * hiddenLayer[lastIndex].getNeuron(jj).getWeight(i);
            }
            outterLayer.getNeuron(i).setValue(activateOutterFunction.compute(value));
        }
    }

    public double[] test(double[][] inputValues) {
        double[] answers = new double[inputValues[0].length];
        for (int i = 0; i < inputValues[0].length; i++) {
            for (int j = 0; j < inputValues.length; j++) {
                sensors.getNeuron(j).setValue(inputValues[j][i]);
            }
            countOutter();
            answers[i] = outterLayer.getNeuron(0).getValue();;
        }
        return answers;
    }
}
