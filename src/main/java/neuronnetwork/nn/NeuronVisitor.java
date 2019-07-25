/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn;

import neuronnetwork.nn.neurons.HiddenNeuron;
import neuronnetwork.nn.neurons.InputNeuron;
import neuronnetwork.nn.neurons.OutputNeuron;

public interface NeuronVisitor {

    public void visitInputNeuron(InputNeuron n);

    public void visitHiddenNeuron(HiddenNeuron n);

    public void visitOutputNeuron(OutputNeuron n);
}
