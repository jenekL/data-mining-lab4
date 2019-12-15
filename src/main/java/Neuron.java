/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jenya
 */
public class Neuron
{
    private double value;
    private double[] weights;
    private double threshold;
    
    public Neuron(int countLinks){
        value = 0;
        weights = new double [countLinks];
        for(int i = 0; i < countLinks; i++){
            weights[i] = Math.random() - 0.5; 
        }
        threshold = Math.random()*0.3 + 0.1;
    }
    
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    public double getWeight(int i){
        return weights[i];
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    
    public void addWeight(int i, double weight){
        weights[i] += weight;
    }
}
