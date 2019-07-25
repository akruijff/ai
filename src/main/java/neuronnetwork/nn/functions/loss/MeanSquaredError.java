/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn.functions.loss;

import static java.lang.Math.pow;
import neuronnetwork.nn.functions.LossFunction;

public class MeanSquaredError
        implements LossFunction {

    @Override
    public double apply(double desired, double actual) {
        return pow(desired - actual, 2);
    }

    @Override
    public double derivative(double desired, double actual) {
        return actual - desired;
    }
}
