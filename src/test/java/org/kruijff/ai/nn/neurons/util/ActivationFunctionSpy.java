/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.neurons.util;

import org.kruijff.ai.nn.functions.ActivationFunction;

public class ActivationFunctionSpy
        implements ActivationFunction {

    private int count;

    @Override
    public double apply(double t) {
        ++count;
        return t;
    }

    public Object count() {
        return count;
    }
}
