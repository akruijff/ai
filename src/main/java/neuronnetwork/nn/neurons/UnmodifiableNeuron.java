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
    public Double output() {
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
    public LossFunction loss() {
        return adaptee.loss();
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
