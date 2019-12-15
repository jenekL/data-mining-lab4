/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jenya
 */
public class HyperbolicFunction implements IFunction {
     private double alpha;
    
    public HyperbolicFunction(double alpha){
        this.alpha = alpha;
    }
    @Override
    public double compute(double x) {
        return Math.tanh((x/alpha));
    }

    @Override
    public double computeFirstDerivative(double x) {
        double com = this.compute(x);
        return (1 + com ) * (1 - com);
    }
}
