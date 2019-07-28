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
package org.kruijff.ai.nn.mpl;

import org.kruijff.ai.nn.Connection;
import org.kruijff.ai.nn.Neuron;
import org.kruijff.ai.nn.NeuronVisitor;
import org.kruijff.ai.nn.neurons.HiddenNeuron;
import org.kruijff.ai.nn.neurons.InputNeuron;
import org.kruijff.ai.nn.neurons.OutputNeuron;

public class SumKoutputs
        implements NeuronVisitor {

    private final double learningRate;
    private final double momentum;
    private double total = 0d;

    public SumKoutputs(double learningRate, double momentum) {
        this.learningRate = learningRate;
        this.momentum = momentum;
    }

    @Override
    public String toString() {
        return Double.toString(total);
    }

    @Override
    public void visitInputNeuron(InputNeuron n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visitHiddenNeuron(HiddenNeuron n) {
        for (Connection con : n.outputConnections()) {
            Neuron to = con.to();
            double w = con.weight(); // wjk
            double a = to.activating().derivative(to.output()); // a = ak * (1 - ak)
            double t = sumKoutputs(con); // -(desired - ak)
            total += w * a * t;
        }
    }

    private double sumKoutputs(Connection con) {
        SumKoutputs s = new SumKoutputs(learningRate, momentum);
        con.to().accept(s); // -(desired - ak)
        return s.total();
    }

    @Override
    public void visitOutputNeuron(OutputNeuron n) {
        NeuralNetwork network = (NeuralNetwork) n.network();
        total = network.loss(network.getDesiredOutput(n), n.output()); // -(desired - ak)
    }

    public double total() {
        return total;
    }
}
