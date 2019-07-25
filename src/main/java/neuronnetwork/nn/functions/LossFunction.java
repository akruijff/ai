/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn.functions;

public interface LossFunction {

    public double apply(double desired, double actual);

    public double derivative(double desired, double actual);
}
