/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Jenya
 */
public class IAD_4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int size = 500;
        int sizeNeuronsPerLayout = 5;
        double a = 3.1;
        double b = 20;
        double alpha = 10;
        int numOfLayoutsHidden = 1;
        int numOfNeuronsPerLayoutHidden = 50;
        int numOfNeuronsInOut = 1;
        double speedStudying = 0.009;
        double accuracy = 0.000001;
        int numOfEpoch = 100;

        Handler handler = new Handler(size, sizeNeuronsPerLayout,
                a, b);

        handler.createNET(numOfLayoutsHidden, numOfNeuronsPerLayoutHidden,
                numOfNeuronsInOut, speedStudying,
                accuracy, numOfEpoch,
                new HyperbolicFunction(alpha), new LinearFunction());

        new MainFrame(handler);
    }
}
