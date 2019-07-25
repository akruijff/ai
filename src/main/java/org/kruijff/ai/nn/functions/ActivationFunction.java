/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.functions;

import java.util.function.Function;

public interface ActivationFunction
        extends Function<Double, Double> {

    @Override
    public Double apply(Double t);

    default Double derivative(Double t) {
        return 1d;
    }
}
