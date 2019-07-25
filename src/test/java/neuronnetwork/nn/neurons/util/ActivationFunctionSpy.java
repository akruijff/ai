/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn.neurons.util;

import neuronnetwork.nn.functions.ActivationFunction;

public class ActivationFunctionSpy
        implements ActivationFunction {

    private int count;

    @Override
    public Double apply(Double t) {
        ++count;
        return t;
    }

    public Object count() {
        return count;
    }
}
