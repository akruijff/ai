/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.fc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kruijff.ai.nn.Connection;
import org.kruijff.ai.nn.Neuron;
import org.kruijff.ai.nn.neurons.AbstractNeuron;
import org.kruijff.ai.nn.neurons.HiddenNeuron;
import org.kruijff.ai.nn.neurons.InputNeuron;
import org.kruijff.ai.nn.neurons.OutputNeuron;

public class NeuralNetworkBuilder {

    private final String id;
    private final NeuralNetwork network;
    private final Layer<InputNeuron> input = new Layer<>("input");
    private final List<Layer<HiddenNeuron>> hidden = new ArrayList<>();
    private final Layer<OutputNeuron> output = new Layer<>("output");

    public NeuralNetworkBuilder(String id) {
        this.id = id;
        this.network = new NeuralNetwork(id, input, hidden, output);
    }

    @Override
    public String toString() {
        return id;
    }

    public void setInputLayer(int count) {
        input.clear();
        for (int i = 0; i < count; ++i)
            input.add(new InputNeuron("input#" + i, network));
    }

    public void addHiddenLayer(String id, int count) {
        Layer<HiddenNeuron> l = new Layer<>(id);
        for (int i = 0; i < count; ++i)
            l.add(new HiddenNeuron(id + '#' + i, network));
        hidden.add(l);
    }

    public void setOuputLayer(int count) {
        output.clear();
        for (int i = 0; i < count; ++i)
            output.add(new OutputNeuron("output#" + i, network));
    }

    public NeuralNetwork build() {
        buildAllConnections();
        return network;
    }

    private void buildAllConnections() {
        Layer<? extends Neuron> previous = input;
        Map<Connection, Connection> map = new HashMap<>();
        for (Layer<? extends Neuron> current : hidden) {
            buildLayerConnections(previous, current);
            previous = current;
        }
        buildLayerConnections(previous, output);
    }

    private void buildLayerConnections(Layer<? extends Neuron> previous, Layer<? extends Neuron> current) {
        AbstractNeuron bias = new HiddenNeuron("BIAS: " + current.getId(), network);
        buildLayerConnections(bias, current);
        for (Neuron from : previous.neurons())
            buildLayerConnections(from, current);
    }

    private void buildLayerConnections(Neuron from, Layer<? extends Neuron> current) {
        for (Neuron to : current.neurons()) {
            Connection c = new Connection(from, to);
            from.addOutputConnection(c);
            to.addInputConnection(c);
        }
    }
}
