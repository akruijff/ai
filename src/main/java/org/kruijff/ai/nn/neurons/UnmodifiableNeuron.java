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

import java.util.List;
import org.kruijff.ai.nn.Connection;
import org.kruijff.ai.nn.Network;
import org.kruijff.ai.nn.Neuron;
import org.kruijff.ai.nn.NeuronVisitor;
import org.kruijff.ai.nn.functions.ActivationFunction;

public class UnmodifiableNeuron
        implements Neuron {

    private final Neuron adaptee;

    public UnmodifiableNeuron(Neuron adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String toString() {
        return adaptee.toString();
    }

    @Override
    public double output() {
        return adaptee.output();
    }

    @Override
    public Network network() {
        return adaptee.network();
    }

    @Override
    public List<Connection> inputConnections() {
        return adaptee.inputConnections();
    }

    @Override
    public List<Connection> outputConnections() {
        return adaptee.outputConnections();
    }

    @Override
    public ActivationFunction activating() {
        return adaptee.activating();
    }

    @Override
    public void accept(NeuronVisitor visitor) {
        adaptee.accept(visitor);
    }

    @Override
    public void addOutputConnection(Connection c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addInputConnection(Connection c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
