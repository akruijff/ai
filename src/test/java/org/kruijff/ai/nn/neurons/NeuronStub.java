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

public class NeuronStub
        implements Neuron {

    private final String id;
    private final double value;

    public NeuronStub(String id) {
        this(id, 0d);
    }

    public NeuronStub(String id, double value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public double output() {
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
