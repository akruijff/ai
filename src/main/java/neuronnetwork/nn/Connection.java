/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn;

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
