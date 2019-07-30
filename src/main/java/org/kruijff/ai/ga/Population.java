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
package org.kruijff.ai.ga;

import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Population<T> {

    private final Settings<T> settings;
    private final List<T> pool;

    public Population(Settings<T> settings) {
        this.settings = settings;
        pool = new ArrayList<>(settings.poolSize);
    }

    @Override
    public String toString() {
        return "size = " + pool.size();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.pool);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.pool, ((Population<?>) obj).pool);
    }

    public Population<T> init() {
        while (pool.size() < settings.poolSize)
            pool.add(settings.initFunc.get());
        return this;
    }

    public Population<T> evolution(StopCondition<T> stopCondition) {
        Population<T> current = this;
        while (true) {
            Population<T> previous = current;
            current = previous.evolve();
            if (stopCondition.apply(previous, current))
                return current;
        }
    }

    private Population<T> evolve() {
        Population<T> current = selection();
        current.crossover();
        current.mutation();
        return current;
    }

    private Population<T> selection() {
        Population<T> p = new Population<>(settings);
        while (!p.isSelectionTargetMet())
            p.addElement(selectElement());
        return p;
    }

    private boolean isSelectionTargetMet() {
        return pool.size() >= settings.selectionSize;
    }

    private T selectElement() {
        return settings.selectFunc.apply(pool);
    }

    private void addElement(T e) {
        if (!pool.contains(e))
            pool.add(e);
    }

    private void crossover() {
        Population<T> p = new Population<>(settings);
        while (!p.isCrossoverTargetMet(this))
            p.addElement(createElement());
        addAll(p);
    }

    private boolean isCrossoverTargetMet(Population<T> p) {
        return pool.size() + p.pool.size() >= settings.poolSize;
    }

    private T createElement() {
        T first = settings.selectFunc.apply(pool);
        T second = settings.selectFunc.apply(pool);
        return settings.crossoverFunc.apply(first, second);
    }

    private void addAll(Population<T> other) {
        pool.addAll(other.pool);
    }

    private void mutation() {
        for (T e : pool)
            if (shouldMutate())
                settings.mutationFunc.accept(this, e);
    }

    private boolean shouldMutate() {
        return random() < settings.mutationChange;
    }

    public int size() {
        return pool.size();
    }

    public boolean contains(T t) {
        return pool.contains(t);
    }
}
