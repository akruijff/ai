/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.fc;

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
