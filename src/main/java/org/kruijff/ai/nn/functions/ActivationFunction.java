/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.functions;

public interface ActivationFunction {

    public double apply(double t);

    default double derivative(double t) {
        return 1d;
    }
}
