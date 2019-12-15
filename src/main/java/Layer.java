/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jenya
 */
public class Layer {
    
    private Neuron[] neurons;

    public Layer(int size, int cLinks) {
        neurons = new Neuron[size];
        for(int i = 0; i < size; i++){
            neurons[i] = new Neuron(cLinks);
        }
    }
    
    public void setInputValues( double [] values){
        
        for(int i = 0; i < neurons.length; i++){
            neurons[i].setValue(values[i]);
        }
    }
    
    public int size(){
        return neurons.length;
    }
    
    public Neuron getNeuron(int i){
        return neurons[i];
    }
}
