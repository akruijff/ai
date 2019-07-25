/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.fc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kruijff.ai.nn.AbstractNetwork;
import org.kruijff.ai.nn.Neuron;
import org.kruijff.ai.nn.functions.LossFunction;
import org.kruijff.ai.nn.functions.loss.MeanSquaredErrorLossFunction;
import org.kruijff.ai.nn.neurons.HiddenNeuron;
import org.kruijff.ai.nn.neurons.InputNeuron;
import org.kruijff.ai.nn.neurons.OutputNeuron;

public class NeuralNetwork
        extends AbstractNetwork {

    private final String id;
    private final Layer<InputNeuron> input;
    private final List<Layer<HiddenNeuron>> hidden;
    private final Layer<OutputNeuron> output;
    private final LossFunction loss = new MeanSquaredErrorLossFunction();
    private final BackpropagationFunction backpropagation = new BackpropagationFunction();

    public NeuralNetwork(String id, Layer<InputNeuron> input, List<Layer<HiddenNeuron>> hidden, Layer<OutputNeuron> output) {
        this.id = id;
        this.input = input;
        this.hidden = hidden;
        this.output = output;
    }

    @Override
    public String toString() {
        return id;
    }

    public void train(double[][] inputs, double[][] expectedOutputs, StopCondition condition) {
        int runs = 0;
        double error;
        do {
            ++runs;
            error = 0;
            for (int i = 0; i < inputs.length; ++i)
                error += trainStep(inputs[i], expectedOutputs[i]);
        } while (!condition.apply(runs, error / inputs.length));
    }

    private double trainStep(double[] inputs, double[] expectedOutputs) {
        double[] actualOutputs = apply(inputs); // Required call for applyBackpropagation
        double error = calculateError(expectedOutputs, actualOutputs);
        applyBackpropagation(expectedOutputs);
        return error;
    }

    @Override
    public double[] apply(double... input) {
        checkInputs(input);
        setInputs(input);
        return calculateOutputs();
    }

    private void checkInputs(double[] input)
            throws RuntimeException {
        if (input.length != this.input.neurons().size())
            throw new RuntimeException();
    }

    private void setInputs(double[] input) {
        int i = -1;
        for (InputNeuron n : this.input.neurons())
            n.setOutput(input[++i]);
    }

    private double[] calculateOutputs() {
        int i = -1;
        double[] arr = new double[output.neurons().size()];
        for (OutputNeuron n : output.neurons())
            arr[++i] = n.output();
        return arr;
    }

    private double calculateError(double[] expectedOutputs, double[] actualOutputs) {
        double error = 0d;
        for (int j = 0; j < expectedOutputs.length; ++j)
            error += loss.apply(actualOutputs[j], expectedOutputs[j]);
        return error / expectedOutputs.length;
    }

    private void applyBackpropagation(double[] expectedOutputs) {
        setDesiredOutput(expectedOutputs);
        output.accept(backpropagation);
        for (int j = hidden.size(); j > 0;)
            hidden.get(--j).accept(backpropagation);
        this.expectedOutputs.clear();
    }

    private Map<Neuron, Double> expectedOutputs = new HashMap<>();

    private void setDesiredOutput(double[] expectedOutputs) {
        int i = -1;
        for (OutputNeuron n : output.neurons())
            this.expectedOutputs.put(n, expectedOutputs[++i]);
    }

    public double getDesiredOutput(OutputNeuron n) {
        return expectedOutputs.get(n);
    }

    double loss(double desiredOutput, double output) {
        return loss.derivative(desiredOutput, output);
    }

    public NeuralNetwork setLoss(LossFunction loss) {
        this.loss = loss;
        return this;
    }
}
