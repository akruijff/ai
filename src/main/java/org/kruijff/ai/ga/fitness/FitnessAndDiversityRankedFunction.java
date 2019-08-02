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
package org.kruijff.ai.ga.fitness;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.kruijff.ai.ga.Chromosome;

public class FitnessAndDiversityRankedFunction<T extends Chromosome>
        implements BiFunction<List<T>, List<T>, T> {

    protected double r;
    private final double pc = .1;
    private int i;
    private int last;

    @Override
    public T apply(List<T> source, List<T> nextPool) {
        r = Math.random();

        i = 0;
        last = source.size() - nextPool.size() - 1;
        List<Tupel<T>> list = createTupels(source, nextPool);
        Collections.sort(list);

        Tupel<T> f = null;
        for (Tupel<T> e : list) {
            f = e;
            if (isChromosomeSelected(e))
                return e.chromosome();
        }
        return f.chromosome();
    }

    private List<Tupel<T>> createTupels(List<T> source, List<T> nextPool) {
        Map<T, Double> fitnessMap = new HashMap<>(2 * source.size());
        for (T s : source)
            fitnessMap.put(s, s.fitness());

        Map<T, Double> diversityMap = new HashMap<>(2 * source.size());
        for (T s : source)
            diversityMap.put(s, nextPool.stream().map(n -> s.partialDiversity(n)).reduce(0d, (a, b) -> a + b));

        Boundry fitnessBoundry;
        {
            double min = POSITIVE_INFINITY, max = NEGATIVE_INFINITY;
            for (T s : source) {
                double v = fitnessMap.get(s);
                if (v < min)
                    min = v;
                if (v > max)
                    max = v;
            }
            fitnessBoundry = new Boundry(min, max);
        }

        Boundry diversityBoundry;
        {
            double min = POSITIVE_INFINITY, max = NEGATIVE_INFINITY;
            for (T s : source) {
                double v = diversityMap.get(s);
                if (v < min)
                    min = v;
                if (v > max)
                    max = v;
            }
            diversityBoundry = new Boundry(min, max);
        }

        List<Tupel<T>> list = new ArrayList<>();
        for (T s : source)
            if (!nextPool.contains(s)) {
                double f = fitnessBoundry.normalize(fitnessMap.get(s));
                double d = diversityBoundry.normalize(diversityMap.get(s));
                list.add(new Tupel(s, f, d));
            }
        return list;
    }

    protected boolean isChromosomeSelected(Tupel<T> e) {
        r -= calculateChanceThisChromosomeIsSelected();
        return r <= 0;
    }

    private double calculateChanceThisChromosomeIsSelected() {
        double c = i == 0 ? pc
                : i < last ? Math.pow(1 - pc, i) * pc
                        : Math.pow(1 - pc, i);
        ++i;
        return c;
    }
}
