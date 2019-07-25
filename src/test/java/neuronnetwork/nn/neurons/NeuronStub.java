/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn.neurons;

import java.util.List;
import neuronnetwork.nn.Connection;
import neuronnetwork.nn.Network;
import neuronnetwork.nn.Neuron;
import neuronnetwork.nn.NeuronVisitor;
import neuronnetwork.nn.functions.ActivationFunction;
import neuronnetwork.nn.functions.LossFunction;

public class NeuronStub
        implements Neuron {

    private final String id;
    private final Double value;

    public NeuronStub(String id) {
        this(id, 0d);
    }

    public NeuronStub(String id, Double value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public Double output() {
        return value;
    }

    @Override
    public List<Connection> inputConnections() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Connection> outputConnections() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Network network() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ActivationFunction activating() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LossFunction loss() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void accept(NeuronVisitor visitor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addOutputConnection(Connection c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addInputConnection(Connection c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
