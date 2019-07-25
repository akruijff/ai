/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.ga;

import static java.lang.Math.random;
import java.util.Arrays;
import java.util.function.Supplier;

public class Population {

    private final Settings settings;
    private final DNA[] pool;

    public static Population init(Supplier<DNA> supplier, int n) {
        Population p = new Population(new Settings(n));
        for (int i = 0; i < n; ++i)
            p.pool[i] = supplier.get();
        return p;
    }

    private Population(Settings settings) {
        this.settings = settings;
        this.pool = new DNA[settings.capacity];
    }

    public Population selection() {
        Arrays.sort(pool, (left, right) -> settings.fitnesse.apply(left) - settings.fitnesse.apply(right));
        Population p = new Population(settings);
        p.copy(this, 0, settings.keepCount);
        p.copyByCrossover(this, settings.keepCount, p.pool.length);
        p.mutationByChance();
        return p;
    }

    private void copy(Population other, int offset, int n) {
        n += offset;
        for (int i = offset; i < n; ++i)
            pool[i] = other.pickRandomDNA();
    }

    private void copyByCrossover(Population other, int offset, int n) {
        n += offset;
        for (int i = offset; i < n; ++i)
            pool[i] = other.crossoverDNA();
    }

    private DNA crossoverDNA() {
        DNA first = pickRandomDNA();
        DNA second = pickRandomDNA();
        return first.crossover(second);
    }

    private DNA pickRandomDNA() {
        int last = pool.length - 1;
        for (int i = 0; i < last; ++i)
            if (random() < settings.selectionChange)
                return pool[i];
        return pool[last];
    }

    private void mutationByChance() {
        for (int i = 0; i < pool.length; ++i)
            pool[i].mutation(settings);
    }
}
