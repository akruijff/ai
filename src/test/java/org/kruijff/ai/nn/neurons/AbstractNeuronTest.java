/*
 * Copyright Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.kruijff.ai.nn.neurons;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kruijff.ai.nn.Connection;
import org.kruijff.ai.nn.Neuron;
import org.kruijff.ai.nn.neurons.util.ActivationFunctionSpy;
import org.kruijff.ai.nn.neurons.util.SummingFunctionStub;

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
        n.addInputConnection(new Connection(from, n, .5));
        n.setActivating(actFunc);
        assertEquals(8d, n.output(), .1);
        assertEquals(1, actFunc.count());
        assertEquals(1, n.inputConnections().size());
    }

    @Test
    @Ignore
    public void addOutputConnection() {
        ActivationFunctionSpy actFunc = new ActivationFunctionSpy();
        HiddenNeuron to = new HiddenNeuron("Test", null);
        to.setActivating(actFunc);
        n.addOutputConnection(new Connection(n, to));
        for (Connection c : n.outputConnections())
            c.to().output();
        assertEquals(1, actFunc.count());
        assertEquals(1, n.outputConnections().size());
    }
}
