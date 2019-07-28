/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.functions.activating;

import org.kruijff.ai.nn.functions.ActivationFunction;

public class StepFunction
        implements ActivationFunction {

    private double threshold;
    private Double high;
    private Double low;

    public StepFunction() {
        this(.5d);
    }

    public StepFunction(double treshold) {
        this(treshold, 1d, 0d);
    }

    public StepFunction(double treshold, double high, double low) {
        this.threshold = treshold;
        this.high = high;
        this.low = low;
    }

    @Override
    public double apply(double t) {
        return t >= threshold ? high : low;
    }

    @Override
    public double derivative(double t) {
        return 1d;
    }
}
