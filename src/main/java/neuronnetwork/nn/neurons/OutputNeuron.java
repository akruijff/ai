/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn.neurons;

import neuronnetwork.nn.Network;
import neuronnetwork.nn.NeuronVisitor;

public class OutputNeuron
        extends AbstractNeuron {

    public OutputNeuron(String id, Network network) {
        super(id, network);
    }

    @Override
    public void accept(NeuronVisitor visitor) {
        visitor.visitOutputNeuron(this);
    }
}
