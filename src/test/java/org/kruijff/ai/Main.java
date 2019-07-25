/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai;

import org.kruijff.ai.nn.Network;
import org.kruijff.ai.nn.fc.NeuralNetwork;
import org.kruijff.ai.nn.fc.NeuralNetworkBuilder;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class Main {

    public static void main(String[] args) {
        double inputs[][] = {{1, 1}, {1, 0}, {0, 1}, {0, 0}};
        double expectedOutputs[][] = {{0}, {1}, {1}, {0}};
        NeuralNetwork network = setupNetwork();

        System.out.println("   Before:");
        showResults(network, inputs);

        network.train(inputs, expectedOutputs, (int runs, double error) -> runs >= 50000 || error < 0.001);

        System.out.println("   After:");
        showResults(network, inputs);
    }

    private static void showResults(Network network, double[][] inputs) {
        for (int i = 0; i < inputs.length; ++i) {
            double[] output = network.apply(inputs[i]);
            System.out.println("" + inputs[i][0] + " XOR " + inputs[i][1] + " == " + output[0]);
        }
        System.out.println("");
    }

    private static NeuralNetwork setupNetwork() {
        NeuralNetworkBuilder n = new NeuralNetworkBuilder("Network");
        n.setInputLayer(2);
        n.setOuputLayer(1);
        n.addHiddenLayer("hidden 1", 4);
        return n.build();
    }

    // https://github.com/DanielaKolarova/NeuralNetworks/blob/master/src/main/java/edu/neuralnet/core/nn/NeuralNet.java
    // https://medium.com/@erikhallstrm/backpropagation-from-the-beginning-77356edf427d
    // https://towardsdatascience.com/everything-you-need-to-know-about-neural-networks-and-backpropagation-machine-learning-made-easy-e5285bc2be3a
    // https://www.edureka.co/blog/backpropagation/
    // https://isaacchanghau.github.io/post/loss_functions/
    // https://www.derivative-calculator.net/
    // https://www.integral-calculator.com/
}
