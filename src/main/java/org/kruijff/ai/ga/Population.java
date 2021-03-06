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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Population<T extends Chromosome> {

    private final Settings<T> settings;
    private final List<T> pool;
    private final List<T> elite;
    private int preventLoop;
    private PopulationListenerCollection<T> listeners = new PopulationListenerCollection<>();

    public Population(Settings<T> settings) {
        this.settings = settings;
        pool = new ArrayList<>(settings.poolSize);
        elite = new ArrayList<>(settings.eliteSize);
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
        sort();
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

        Population<T> current = new Population<>(settings);
        current.listeners = listeners;

        current.eliteSelection(this);
        current.selection(this);
        current.crossover(this);
        current.mutation();
        current.sort();

        ++settings.evolutionCount;
        listeners.endEvolvingPopulation(this);
        return current;
    }

    private void eliteSelection(Population<T> source) {
        for (T e : source.getElements())
            if (!isEliteSelectionDone())
                addElite(e);
            else
                return;
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for eliteSelection">
    private boolean isEliteSelectionDone() {
        return pool.size() >= settings.eliteSize;
    }

    private void addElite(T e) {
        pool.add(e);
        elite.add(e);
        listeners.eliteSelectedChromosome(e);
    }
    //</editor-fold>

    private void selection(Population<T> source) {
        while (!isSelectionDone()) {
            T e = settings.selectFunc.apply(source.pool, pool);
            if (!pool.contains(e))
                addSelected(e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for selection">
    private boolean isSelectionDone() {
        return pool.size() >= settings.selectionNumber();
    }

    private void addSelected(T e) {
        pool.add(e);
        listeners.selectedChromosome(e);
    }
    //</editor-fold>

    private void mutation() {
        MutationManager manager = new MutationManager();
        for (T e : pool)
            if (shouldMutate(e)) {
                T mutation = settings.mutationFunc.apply(this, e);
                manager.register(e, mutation);
            }
        manager.storeMutation(pool);
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for mutation">
    private boolean shouldMutate(T e) {
        return random() < settings.mutationChance && !elite.contains(e);
    }

    private class MutationManager {

        private final List<T> removals = new ArrayList<>();
        private final List<T> mutations = new ArrayList<>();

        private void register(T e, T mutation) {
            removals.add(e);
            mutations.add(mutation);
            listeners.mutatedChromosome(mutation);
        }

        private void storeMutation(List<T> pool) {
            pool.removeAll(removals);
            pool.addAll(mutations);
        }
    }
    //</editor-fold>

    private void crossover(Population<T> source) {
        for (int i = 0; i < settings.crossoverNumber(); ++i) {
            T e = createChild(source);
            addChild(e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for crossover">
    private T createChild(Population<T> source) {
        T first = selectRandom(source);
        T second = selectRandom(source);
        return settings.crossoverFunc.apply(first, second);
    }

    private void addChild(T e) {
        pool.add(e);
        listeners.crossoverChromosome(e);
    }

    private T selectRandom(Population<T> source) {
        int index = (int) (random() * source.size());
        return source.pool.get(index);
    }
    //</editor-fold>

    private void sort() {
        sort((left, right) -> left.fitness() < right.fitness() ? -1 : 1);
    }

    private final void sort(Comparator<T> comperator) {
        Collections.sort(pool, comperator);
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

    private class PopulationListenerCollection<T extends Chromosome>
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
        public void eliteSelectedChromosome(T c) {
            for (PopulationListener<T> l : list)
                l.eliteSelectedChromosome(c);
        }

        @Override
        public void selectedChromosome(T c) {
            for (PopulationListener<T> l : list)
                l.selectedChromosome(c);
        }

        @Override
        public void mutatedChromosome(T c) {
            for (PopulationListener<T> l : list)
                l.mutatedChromosome(c);
        }

        @Override
        public void crossoverChromosome(T c) {
            for (PopulationListener<T> l : list)
                l.crossoverChromosome(c);
        }

        @Override
        public void endEvolvingPopulation(Population<T> p) {
            for (PopulationListener<T> l : list)
                l.endEvolvingPopulation(p);
        }
    }
}
