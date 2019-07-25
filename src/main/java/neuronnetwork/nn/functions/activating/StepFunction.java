/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn.functions.activating;

import neuronnetwork.nn.functions.ActivationFunction;

public class StepFunction
        implements ActivationFunction {

    private Double threshold;
    private Double high;
    private Double low;

    public StepFunction() {
        this(.5d);
    }

    public StepFunction(Double treshold) {
        this(treshold, 1d, 0d);
    }

    public StepFunction(Double treshold, Double high, Double low) {
        this.threshold = treshold;
        this.high = high;
        this.low = low;
    }

    @Override
    public Double apply(Double t) {
        return t >= threshold ? high : low;
    }

    @Override
    public Double derivative(Double t) {
        return 1d;
    }
}
