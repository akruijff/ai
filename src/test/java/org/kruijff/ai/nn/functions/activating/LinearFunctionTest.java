/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.functions.activating;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.kruijff.ai.nn.functions.ActivationFunction;

public class LinearFunctionTest {

    @Test
    public void apply_Baseline() {
        ActivationFunction func = new LinearFunction();
        double result = func.apply(0);
        assertEquals(0d, result, 0.1d);
    }

    @Test
    public void derivative_Baseline() {
        ActivationFunction func = new LinearFunction();
        double result = func.derivative(0);
        assertEquals(0d, result, 0.1d);
    }

    @Test
    public void apply_NonZero() {
        ActivationFunction func = new LinearFunction(1d);
        double result = func.apply(0);
        assertEquals(1d, result, 0.1d);
    }

    @Test
    public void derivative_NonZero() {
        ActivationFunction func = new LinearFunction(1d);
        double result = func.derivative(0);
        assertEquals(0d, result, 0.1d);
    }
}
