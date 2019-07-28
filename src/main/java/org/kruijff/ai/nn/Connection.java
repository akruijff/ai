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
package org.kruijff.ai.nn;

import static java.lang.Math.random;
import java.util.Objects;
// import neuronnetwork.nn.neurons.UnmodifiableNeuron;

public class Connection {

    private final Neuron from;
    private final Neuron to;
    private double weigth;

    public Connection(Neuron from, Neuron to) {
        this(from, to, random());
    }

    public Connection(Neuron from, Neuron to, double weigth) {
        this.from = from;
        this.to = to;
        this.weigth = weigth;
    }

    @Override
    public String toString() {
        return from + " -> " + to + ": " + weigth;
    }

    @Override
    public int hashCode() {
        return 5 * 37 * 37
                + 37 * Objects.hashCode(this.from)
                + Objects.hashCode(this.to);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && equals((Connection) obj);
    }

    public boolean equals(Connection other) {
        return Objects.equals(this.from, other.from) && Objects.equals(this.to, other.to);
    }

    public Neuron from() {
        return from;
//        return new UnmodifiableNeuron(from);
    }

    public Neuron to() {
        return to;
//        return new UnmodifiableNeuron(to);
    }

    public double weight() {
        return weigth;
    }

    public void setWeight(double weigth) {
        this.weigth = weigth;
    }
}
