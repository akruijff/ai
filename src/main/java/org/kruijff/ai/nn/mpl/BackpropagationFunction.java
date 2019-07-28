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

import java.util.HashMap;
import java.util.Map;
import org.kruijff.ai.nn.Connection;
import org.kruijff.ai.nn.Neuron;
import org.kruijff.ai.nn.NeuronVisitor;
import org.kruijff.ai.nn.neurons.HiddenNeuron;
import org.kruijff.ai.nn.neurons.InputNeuron;
import org.kruijff.ai.nn.neurons.OutputNeuron;

class BackpropagationFunction
        implements NeuronVisitor {

    private final Map<Connection, Double> map = new HashMap<>(16);
    private final double learningRate;
    private final double momentum;

    BackpropagationFunction() {
        this(2.9, 0.7);
    }

    BackpropagationFunction(double learningRate, double momentum) {
        this.learningRate = learningRate;
        this.momentum = momentum;
    }

    @Override
    public void visitInputNeuron(InputNeuron n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visitHiddenNeuron(HiddenNeuron n) {
        visitNeuron(n);
    }

    @Override
    public void visitOutputNeuron(OutputNeuron n) {
        visitNeuron(n);
    }

    private void visitNeuron(Neuron n) {
        double t = totalSumOutputs(n);
        double a = n.activating().derivative(n.output()); // aj * (1 - aj)
        for (Connection con : n.inputConnections()) {
            double o = con.from().output();
            double partialDerivative = o * a * t;
            double deltaWeight = -learningRate * partialDerivative;
            double newWeight = con.weight() + deltaWeight;
            double prevDeltaWeight = putDeltaWeigth(con, deltaWeight);
            con.setWeight(newWeight + momentum * prevDeltaWeight);
        }
    }

    private double totalSumOutputs(Neuron n) {
        SumKoutputs s = new SumKoutputs(learningRate, momentum);
        n.accept(s);
        return s.total();
    }

    private double putDeltaWeigth(Connection con, double deltaWeight) {
        Double prev = map.put(con, deltaWeight);
        return prev == null ? 0d : prev;
    }
}
