/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
