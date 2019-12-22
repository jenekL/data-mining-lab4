/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Jenya
 */
public class SigmoidFunction implements IFunction {
    private double alpha;

    SigmoidFunction(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public double compute(double x) {
        double r = (1 / (1 + Math.exp(-1 * alpha * x)));
        return r;
    }

    @Override
    public double computeFirstDerivative(double x) {
        double com = this.compute(x);
        return com * (1 - com);
    }
}
