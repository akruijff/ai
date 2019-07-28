/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.functions.activating;

import static java.lang.Double.max;
import org.kruijff.ai.nn.functions.ActivationFunction;

public class RectifiedLinearUnitFunction
        implements ActivationFunction {

    @Override
    public double apply(double t) {
        return max(0, t);
    }

    @Override
    public double derivative(double t) {
        return t >= 0 ? 1d : 0d;
    }
}
