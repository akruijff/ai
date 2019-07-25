/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.functions.activating;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import org.kruijff.ai.nn.functions.ActivationFunction;

public class SinusoidFunction
        implements ActivationFunction {

    @Override
    public Double apply(Double t) {
        return sin(t);
    }

    @Override
    public Double derivative(Double t) {
        return cos(t);
    }
}
