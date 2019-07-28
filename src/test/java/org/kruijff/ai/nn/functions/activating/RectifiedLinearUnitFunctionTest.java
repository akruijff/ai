/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.functions.activating;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.kruijff.ai.nn.functions.ActivationFunction;

public class RectifiedLinearUnitFunctionTest {

    private ActivationFunction func;

    @Before
    public void setup() {
        func = new RectifiedLinearUnitFunction();
    }

    @After
    public void teardown() {
        func = null;
    }

    @Test
    public void apply_negative() {
        double result = func.apply(-1d);
        assertEquals(0d, result, 0.1d);
    }

    @Test
    public void apply_zero() {
        double result = func.apply(0d);
        assertEquals(0d, result, 0.1d);
    }

    @Test
    public void apply_positive() {
        double result = func.apply(1d);
        assertEquals(1d, result, 0.1d);
    }

    @Test
    public void derivative_negative() {
        double result = func.derivative(-1d);
        assertEquals(0d, result, 0.1d);
    }

    @Test
    public void derivative_zero() {
        double result = func.derivative(0d);
        assertEquals(1d, result, 0.1d);
    }

    @Test
    public void derivative_positive() {
        double result = func.derivative(1d);
        assertEquals(1d, result, 0.1d);
    }
}
