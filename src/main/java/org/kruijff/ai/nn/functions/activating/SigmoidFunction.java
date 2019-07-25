/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.functions.activating;

import static java.lang.Math.exp;
import org.kruijff.ai.nn.functions.ActivationFunction;

public class SigmoidFunction
        implements ActivationFunction {

    private double slope;

    public SigmoidFunction() {
        this(1d);
    }

    public SigmoidFunction(double slope) {
        this.slope = slope;
    }

    @Override
    public Double derivative(Double x) {
        return x * (1d - x);
    }

    @Override
    public Double apply(Double x) {
        return 1d / (1 + exp(-slope * x));
    }
}
