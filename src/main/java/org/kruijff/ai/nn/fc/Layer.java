/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.fc;

import static java.lang.String.format;
import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import org.kruijff.ai.nn.Neuron;
import org.kruijff.ai.nn.NeuronVisitor;

public class Layer<N extends Neuron> {

    private final String id;
    private final List<N> list = new ArrayList<>();

    public Layer(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return format("%s, size=%d", id, list.size());
    }

    List<N> neurons() {
        return unmodifiableList(list);
    }

    void clear() {
        list.clear();
    }

    void add(N n) {
        list.add(n);
    }

    String getId() {
        return id;
    }

    void accept(NeuronVisitor visitor) {
        for (Neuron n : neurons())
            n.accept(visitor);
    }
}
