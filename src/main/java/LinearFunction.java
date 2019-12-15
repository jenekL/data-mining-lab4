/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jenya
 */
public class LinearFunction implements IFunction {
    public LinearFunction(){}
    @Override
    public double compute(double x) {
        return x;
    }

    @Override
    public double computeFirstDerivative(double x) {
        return 1;
    }
    
}
