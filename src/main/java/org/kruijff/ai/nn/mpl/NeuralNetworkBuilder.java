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
