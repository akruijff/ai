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
    public double output() {
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
