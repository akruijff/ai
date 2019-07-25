/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.neurons;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.kruijff.ai.nn.neurons.util.NeuronVisitorSpy;

public class HiddenNeuronTest {

    private HiddenNeuron n;

    @Before
    public void setup() {
        n = new HiddenNeuron("Test", null);
    }

    @After
    public void teardown() {
        n = null;
    }

    @Test
    public void visit() {
        NeuronVisitorSpy visitor = new NeuronVisitorSpy();
        n.accept(visitor);
        assertEquals(0, visitor.getCountInput());
        assertEquals(1, visitor.getCountHidden());
        assertEquals(0, visitor.getCountOutput());
    }
}
