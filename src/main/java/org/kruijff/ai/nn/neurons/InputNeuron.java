/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.neurons;

import org.kruijff.ai.nn.Network;
import org.kruijff.ai.nn.NeuronVisitor;

public class InputNeuron
        extends AbstractNeuron {

    private double output;

    public InputNeuron(String id, Network network) {
        super(id, network);
    }

    @Override
    public double output() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    @Override
    public void accept(NeuronVisitor visitor) {
        visitor.visitInputNeuron(this);
    }
}
