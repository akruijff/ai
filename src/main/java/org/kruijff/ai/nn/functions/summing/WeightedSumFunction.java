/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.functions.summing;

import java.util.List;
import java.util.function.Function;
import org.kruijff.ai.nn.Connection;

public class WeightedSumFunction
        implements Function<List<Connection>, Double> {

    @Override
    public Double apply(List<Connection> list) {
        Double total = 0d;
        for (Connection c : list)
            total += c.weight() * c.from().output();
        return total;
    }
}
