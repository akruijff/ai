/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.neurons.util;

import java.util.List;
import java.util.function.Function;
import org.kruijff.ai.nn.Connection;

public class SummingFunctionStub
        implements Function<List<Connection>, Double> {

    private int count;
    private final Double value;

    public SummingFunctionStub() {
        this(0d);
    }

    public SummingFunctionStub(Double value) {
        this.value = value;
    }

    @Override
    public Double apply(List<Connection> t) {
        ++count;
        return value;
    }

    public int count() {
        return count;
    }
}
