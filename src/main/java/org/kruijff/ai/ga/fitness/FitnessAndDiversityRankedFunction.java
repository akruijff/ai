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
import org.kruijff.ai.ga.exceptions.SourcePoolEmptyException;

public class FitnessAndDiversityRankedFunction<T extends Chromosome>
        implements BiFunction<List<T>, List<T>, T> {

    private final RandomGenerator randomGenerator;
    private final double pc = .1;

    public FitnessAndDiversityRankedFunction() {
        this.randomGenerator = RandomGenerator.newInstance();
    }

    public FitnessAndDiversityRankedFunction(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public T apply(List<T> sourcePool, List<T> nextPool) {
        if (sourcePool.isEmpty())
            throw new SourcePoolEmptyException();
        List<Tupel<T>> list = new TupelListBuilder(sourcePool, nextPool).createTupelList();
        return new ChromosomeSelector(sourcePool, nextPool, list).select();
    }

    private class TupelListBuilder {

        private final List<T> sourcePool;
        private final List<T> nextPool;
        private final Map<T, Double> fitnessMap;
        private final Map<T, Double> diversityMap;
        private final Boundry fitnessBoundry;
        private final Boundry diversityBoundry;

        private TupelListBuilder(List<T> sourcePool, List<T> nextPool) {
            this.sourcePool = sourcePool;
            this.nextPool = nextPool;
            fitnessMap = createFitnessMap(sourcePool);
            diversityMap = createDiversityMap(sourcePool);
            fitnessBoundry = createBoundry(sourcePool, fitnessMap);
            diversityBoundry = createBoundry(sourcePool, diversityMap);
        }

        private Map<T, Double> createFitnessMap(List<T> sourcePool) {
            Map<T, Double> map = new HashMap<>(2 * sourcePool.size());
            sourcePool.forEach(s -> map.put(s, s.fitness()));
            return map;
        }

        private Map<T, Double> createDiversityMap(List<T> sourcePool) {
            Map<T, Double> map = new HashMap<>(2 * sourcePool.size());
            sourcePool.forEach(s -> map.put(s, diversity(s)));
            return map;
        }

        private Double diversity(T s) {
            return nextPool.stream().
                    map(n -> s.partialDiversity(n)).
                    reduce(0d, (a, b) -> a + b);
        }

        private Boundry createBoundry(List<T> source, Map<T, Double> map) {
            double min = POSITIVE_INFINITY, max = NEGATIVE_INFINITY;
            for (T s : source) {
                min = Math.min(min, map.get(s));
                max = Math.max(max, map.get(s));
            }
            return new Boundry(min, max);
        }

        private List<Tupel<T>> createTupelList() {
            return sourcePool.stream()
                    .filter(s -> !nextPool.contains(s))
                    .map(s -> createTupel(s))
                    .collect(ArrayList::new, List::add, List::addAll);
        }

        private Tupel<T> createTupel(T s) {
            double f = fitnessBoundry.normalize(fitnessMap.get(s));
            double d = diversityBoundry.normalize(diversityMap.get(s));
            return new Tupel<>(s, f, d);
        }
    }

    private class ChromosomeSelector {

        private final List<T> sourcePool;
        private final List<T> nextPool;
        private final List<Tupel<T>> list;
        private double r;
        private int i;

        private ChromosomeSelector(List<T> sourcePool, List<T> nextPool, List<Tupel<T>> list) {
            this.sourcePool = sourcePool;
            this.nextPool = nextPool;
            this.list = list;
            r = randomGenerator.random();
            i = 0;
            Collections.sort(list);
        }

        private T select() {
            Tupel<T> f = list.get(list.size() - 1);
            for (Tupel<T> e : list)
                if (isChromosomeSelected(e))
                    return e.chromosome();
            return f.chromosome();
        }

        private boolean isChromosomeSelected(Tupel<T> e) {
            r -= calculateChanceThisChromosomeIsSelected();
            return r <= 0;
        }

        private double calculateChanceThisChromosomeIsSelected() {
            int last = sourcePool.size() - nextPool.size() - 1;
            double c = i == 0 ? pc
                    : i < last ? Math.pow(1 - pc, i) * pc
                            : Math.pow(1 - pc, i);
            ++i;
            return c;
        }
    }
}
