/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn.neurons.util;

import neuronnetwork.nn.NeuronVisitor;
import neuronnetwork.nn.neurons.HiddenNeuron;
import neuronnetwork.nn.neurons.InputNeuron;
import neuronnetwork.nn.neurons.OutputNeuron;

public class NeuronVisitorSpy
        implements NeuronVisitor {

    private int countInput, countHidden, countOutput;

    @Override
    public void visitInputNeuron(InputNeuron n) {
        ++countInput;
    }

    @Override
    public void visitHiddenNeuron(HiddenNeuron n) {
        ++countHidden;
    }

    @Override
    public void visitOutputNeuron(OutputNeuron n) {
        ++countOutput;
    }

    public int getCountInput() {
        return countInput;
    }

    public int getCountHidden() {
        return countHidden;
    }

    public int getCountOutput() {
        return countOutput;
    }
}
