/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.neurons;

import org.kruijff.ai.nn.neurons.AbstractNeuron;
import org.kruijff.ai.nn.neurons.HiddenNeuron;
import org.kruijff.ai.nn.Connection;
import org.kruijff.ai.nn.Neuron;
import org.kruijff.ai.nn.neurons.util.ActivationFunctionSpy;
import org.kruijff.ai.nn.neurons.util.SummingFunctionStub;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class AbstractNeuronTest {

    private AbstractNeuron n;

    @Before
    public void setup() {
        n = new HiddenNeuron("Test", null);
    }

    @After
    public void teardown() {
        n = null;
    }

    @Test
    public void output() {
        SummingFunctionStub sumFunc = new SummingFunctionStub();
        ActivationFunctionSpy actFunc = new ActivationFunctionSpy();
        n.setSumming(sumFunc);
        n.setActivating(actFunc);
        n.output();
        assertEquals(1, sumFunc.count());
        assertEquals(1, actFunc.count());
    }

    @Test
    public void addInputConnection() {
        Neuron from = new NeuronStub("Test", 16d);
        Connection in = new Connection(from, n, .5);
        n.addInputConnection(in);
        assertEquals(1, n.inputConnections().size());
    }

    @Test
    public void addInputConnection2() {
        ActivationFunctionSpy actFunc = new ActivationFunctionSpy();
        Neuron from = new NeuronStub("Test", 16d);
        Connection in = new Connection(from, n, .5);
        n.addInputConnection(in);
        n.setActivating(actFunc);
        assertEquals(8d, n.output(), .1);
        assertEquals(1, actFunc.count());
        assertEquals(1, n.inputConnections().size());
    }

    @Test
    @Ignore
    public void addOutputConnection() {
        Neuron to = new HiddenNeuron("Test", null);
        Connection out = new Connection(n, to);
        n.addOutputConnection(out);
        fail("Needs an assertion");
        assertEquals(1, n.outputConnections().size());
    }
}
