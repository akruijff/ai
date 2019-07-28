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
