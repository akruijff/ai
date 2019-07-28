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
package org.kruijff.ai;

import org.junit.Assert;
import org.junit.Test;
import org.kruijff.ai.nn.Network;
import org.kruijff.ai.nn.mpl.NeuralNetwork;
import org.kruijff.ai.nn.mpl.NeuralNetworkBuilder;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class Main {

    public static void main(String[] args) {
        double inputs[][] = {{1, 1}, {1, 0}, {0, 1}, {0, 0}};
        double expected[][] = {{0}, {1}, {1}, {0}};
        NeuralNetwork network = setupNetwork();

        showResults("Before:", network, inputs);
        network.train(inputs, expected, (int runs, double error) -> runs >= 50000 || error < 0.01);
        showResults("After:", network, inputs);
    }

    @Test
    public void test() {
        double inputs[][] = {{1, 1}, {1, 0}, {0, 1}, {0, 0}};
        double expected[][] = {{0}, {1}, {1}, {0}};
        NeuralNetwork network = setupNetwork();
        network.train(inputs, expected, (int runs, double error) -> runs >= 500000 || error < 0.001);

        for (int i = 0; i < inputs.length; ++i) {
            double[] actual = network.apply(inputs[i]);
            Assert.assertEquals(expected[i][0], actual[0], .1d);
        }
    }

    private static void showResults(String h, Network network, double[][] inputs) {
        System.out.println("   " + h);
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
