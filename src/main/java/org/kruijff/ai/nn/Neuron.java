/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn;

import java.util.List;
import org.kruijff.ai.nn.functions.ActivationFunction;

public interface Neuron {

    public Double output();

    public List<Connection> inputConnections();

    public List<Connection> outputConnections();

    public Network network();

    public ActivationFunction activating();

    public void accept(NeuronVisitor visitor);

    public void addOutputConnection(Connection c);

    public void addInputConnection(Connection c);
}
