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
import static java.lang.String.format;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Objects;

public class Population<T extends Fitness> {

    private final Settings<T> settings;
    private final List<T> pool;
    private int preventLoop;
    private PopulationListenerCollection<T> listeners = new PopulationListenerCollection<>();

    public Population(Settings<T> settings) {
        this.settings = settings;
        pool = new ArrayList<>(settings.poolSize);
    }

    @Override
    public String toString() {
        return format("size=%d, fitness=(%f, %f, %f)", pool.size(), getMinFitness(), getAvgFitness(), getMaxFitness());
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

    public void addPopulationListener(PopulationListener<T> l) {
        listeners.add(l);
    }

    public Population<T> init() {
        while (pool.size() < settings.poolSize)
            pool.add(settings.initFunc.get());
        Collections.sort(pool);
        listeners.initialPopulation(this);
        return this;
    }

    public Population<T> evolution(StopCondition<T> stopCondition) {
        Population<T> current = this;
        stopCondition.apply(this);
        while (true) {
            Population<T> previous = current;
            current = previous.evolve();
            if (stopCondition.apply(current))
                return stopCondition.get();
        }
    }

    private Population<T> evolve() {
        listeners.startEvolvingPopulation(this);
        Population<T> current = selection();
        current.crossover();
        current.mutation();
        Collections.sort(pool);
        ++settings.evolutionCount;
        listeners.endEvolvingPopulation(this);
        return current;
    }

    private Population<T> selection() {
        Population<T> p = new Population<>(settings);
        p.listeners = listeners;
        while (!p.isSelectionTargetMet()) {
            T e = selectElement();
            if (p.addElement(e))
                listeners.selectedChromosome(e);
        }
        return p;
    }

    private boolean isSelectionTargetMet() {
        return pool.size() >= settings.selectionSize;
    }

    private T selectElement() {
        return settings.selectFunc.apply(pool);
    }

    private boolean addElement(T e) {
        return addElement(e, Collections.EMPTY_LIST);
    }

    private void crossover() {
        Population<T> p = new Population<>(settings);
        while (!p.isCrossoverTargetMet(this)) {
            T e = createElement();
            if (p.addElement(e, pool))
                listeners.crossoverChromosome(e);
        }
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

    private boolean addElement(T e, List<T> other) {
        if (e != null && !pool.contains(e) && !other.contains(e)) {
            pool.add(e);
            return true;
        }
        if (++preventLoop >= settings.preventLoop)
            pool.add(settings.initFunc.get());
        return false;
    }

    private void addAll(Population<T> other) {
        pool.addAll(other.pool);
    }

    private void mutation() {
        for (T e : pool)
            if (shouldMutate()) {
                settings.mutationFunc.accept(this, e);
                listeners.mutatedChromosome(e);
            }
    }

    private boolean shouldMutate() {
        return random() < settings.mutationChance;
    }

    public int size() {
        return pool.size();
    }

    public boolean contains(T t) {
        return pool.contains(t);
    }

    public List<T> getElements() {
        return unmodifiableList(pool);
    }

    public T getWorst() {
        return pool.stream()
                .min((left, right) -> left.fitness() < right.fitness() ? -1 : 1)
                .get();
    }

    public T getBest() {
        return pool.stream()
                .max((left, right) -> left.fitness() < right.fitness() ? -1 : 1)
                .get();
    }

    public double getMinFitness() {
        return pool.stream()
                .map(c -> c.fitness())
                .min((left, right) -> left < right ? -1 : 1)
                .orElse(0d);
    }

    public double getMaxFitness() {
        return pool.stream()
                .map(c -> c.fitness())
                .max((left, right) -> left < right ? -1 : 1)
                .orElse(0d);
    }

    public double getAvgFitness() {
        double sum = pool.stream()
                .map(c -> c.fitness())
                .reduce(0d, (a, b) -> a + b);
        return pool.size() > 0 ? sum / pool.size() : 0d;
    }

    private class PopulationListenerCollection<T extends Fitness>
            implements PopulationListener<T> {

        private List<PopulationListener<T>> list = new ArrayList<>();

        public void add(PopulationListener<T> l) {
            list.add(l);
        }

        @Override
        public void initialPopulation(Population<T> p) {
            for (PopulationListener<T> l : list)
                l.initialPopulation(p);
        }

        @Override
        public void startEvolvingPopulation(Population<T> p) {
            for (PopulationListener<T> l : list)
                l.startEvolvingPopulation(p);
        }

        @Override
        public void endEvolvingPopulation(Population<T> p) {
            for (PopulationListener<T> l : list)
                l.endEvolvingPopulation(p);
        }

        @Override
        public void selectedChromosome(T c) {
            for (PopulationListener<T> l : list)
                l.selectedChromosome(c);
        }

        @Override
        public void crossoverChromosome(T c) {
            for (PopulationListener<T> l : list)
                l.crossoverChromosome(c);
        }

        @Override
        public void mutatedChromosome(T c) {
            for (PopulationListener<T> l : list)
                l.mutatedChromosome(c);
        }
    }
}
