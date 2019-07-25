/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn.functions.activating;

import neuronnetwork.nn.functions.ActivationFunction;

public class LinearFunction
        implements ActivationFunction {

    private double bias;

    @Override
    public Double apply(Double t) {
        return t + bias;
    }

    @Override
    public Double derivative(Double t) {
        return 0d; // @TODO is this correct?
    }
}
