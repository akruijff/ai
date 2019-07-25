/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn.fc;

import neuronnetwork.nn.Connection;
import neuronnetwork.nn.Neuron;
import neuronnetwork.nn.NeuronVisitor;
import neuronnetwork.nn.neurons.HiddenNeuron;
import neuronnetwork.nn.neurons.InputNeuron;
import neuronnetwork.nn.neurons.OutputNeuron;

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
        total = n.loss().derivative(network.getDesiredOutput(n), n.output()); // -(desired - ak)
    }

    public double total() {
        return total;
    }
}
