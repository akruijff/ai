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

    private Double output;

    public InputNeuron(String id, Network network) {
        super(id, network);
    }

    @Override
    public Double output() {
        return output;
    }

    public void setOutput(Double output) {
        this.output = output;
    }

    @Override
    public void accept(NeuronVisitor visitor) {
        visitor.visitInputNeuron(this);
    }
}
