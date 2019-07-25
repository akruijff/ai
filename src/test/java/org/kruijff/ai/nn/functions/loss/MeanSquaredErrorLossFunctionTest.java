/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.functions.loss;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.kruijff.ai.nn.functions.LossFunction;

public class MeanSquaredErrorLossFunctionTest {

    private LossFunction loss;

    @Before
    public void setup() {
        loss = new MeanSquaredErrorLossFunction();
    }

    @After
    public void teardown() {
        loss = null;
    }

    @Test
    public void apply() {
        assertEquals(9d, loss.apply(5d, 2d), 0.1d);
        assertEquals(9d, loss.apply(2d, 5d), 0.1d);
    }

    @Test
    public void derivative() {
        assertEquals(-3d, loss.derivative(5d, 2d), 0.1d);
        assertEquals(3d, loss.derivative(2d, 5d), 0.1d);
    }
}
