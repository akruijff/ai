/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn.functions.activating;

import static java.lang.Double.max;
import neuronnetwork.nn.functions.ActivationFunction;

public class RectifiedLinearUnitFunction
        implements ActivationFunction {

    @Override
    public Double apply(Double t) {
        return max(0, t);
    }

    @Override
    public Double derivative(Double t) {
        return t >= 0 ? 1d : 0d;
    }
}
