/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.neurons;

import static java.lang.String.format;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.kruijff.ai.nn.Connection;
import org.kruijff.ai.nn.Network;
import org.kruijff.ai.nn.Neuron;
import org.kruijff.ai.nn.functions.ActivationFunction;
import org.kruijff.ai.nn.functions.activating.SigmoidFunction;
import org.kruijff.ai.nn.functions.summing.WeightedSumFunction;

public abstract class AbstractNeuron
        implements Neuron {

    private static int n = 0;
    private final int id;
    private final String identifier;
    private final WeakReference<Network> network;
    private final List<Connection> inputConnections = new ArrayList<>();
    private final List<Connection> outputConnections = new ArrayList<>();

    private Function<List<Connection>, Double> summing = new WeightedSumFunction();
    private ActivationFunction activating = new SigmoidFunction();

    AbstractNeuron(String id, Network network) {
        this.id = ++n;
        this.identifier = id;
        this.network = new WeakReference<>(network);
    }

    @Override
    public String toString() {
        return format("%d, %s (%d, %d)", id, identifier, inputConnections.size(), outputConnections.size());
    }

    @Override
    public int hashCode() {
        return 17 * 7 + Objects.hashCode(this.identifier);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.identifier, ((AbstractNeuron) obj).identifier);
    }

    @Override
    public Double output() {
        Double sum = summing.apply(inputConnections);
        return activating.apply(sum);
    }

    @Override
    public Network network() {
        return network.get();
    }

    @Override
    public List<Connection> inputConnections() {
        return unmodifiableList(inputConnections);
    }

    @Override
    public List<Connection> outputConnections() {
        return unmodifiableList(outputConnections);
    }

    @Override
    public void addInputConnection(Connection c) {
        inputConnections.add(c);
    }

    @Override
    public void addOutputConnection(Connection c) {
        outputConnections.add(c);
    }

    @Override
    public ActivationFunction activating() {
        return activating;
    }

    public AbstractNeuron setSumming(Function<List<Connection>, Double> summing) {
        this.summing = summing;
        return this;
    }

    public AbstractNeuron setActivating(ActivationFunction activating) {
        this.activating = activating;
        return this;
    }
}
