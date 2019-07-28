/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.functions.activating;

import org.kruijff.ai.nn.functions.ActivationFunction;

public class LinearFunction
        implements ActivationFunction {

    private double bias;

    public LinearFunction() {
        this(0);
    }

    public LinearFunction(double bias) {
        this.bias = bias;
    }

    @Override
    public double apply(double t) {
        return t + bias;
    }

    @Override
    public double derivative(double t) {
        return 0d;
    }
}
