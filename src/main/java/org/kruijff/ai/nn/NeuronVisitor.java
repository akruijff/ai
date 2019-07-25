/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn;

import org.kruijff.ai.nn.neurons.HiddenNeuron;
import org.kruijff.ai.nn.neurons.InputNeuron;
import org.kruijff.ai.nn.neurons.OutputNeuron;

public interface NeuronVisitor {

    public void visitInputNeuron(InputNeuron n);

    public void visitHiddenNeuron(HiddenNeuron n);

    public void visitOutputNeuron(OutputNeuron n);
}
