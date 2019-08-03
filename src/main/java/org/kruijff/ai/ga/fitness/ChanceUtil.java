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
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.util.Collections.sort;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.kruijff.ai.ga.Chromosome;

public class ChanceUtil<T extends Chromosome> {

    public Map<T, Double> rouletteFitness(List<T> source) {
        Map<T, Double> mapFitness = mapFitness(source);
        Boundry boundry = getBoundry(source, s -> mapFitness.get(s));
        Map<T, Double> normalizedFitnessMap = normalizeMap(mapFitness, boundry);
        double sum = getSum(source, s -> normalizedFitnessMap.get(s));
        return collectMap(source, s -> normalizedFitnessMap.get(s) / sum);
    }

    public Map<T, Double> rankedFitness(List<T> source, double pc) {
        Map<T, Double> mapFitness = mapFitness(source);
        sort(source, (l, r) -> (int) (mapFitness.get(r) - mapFitness.get(l)));
        return collectMap(source, new RankedFunction(source, pc));
    }

    public Map<T, Double> rouletteDistance(List<T> source, List<T> next) {
        Map<T, Double> mapDistance = mapDistance(source, next);
        Boundry boundry = getBoundry(source, s -> mapDistance.get(s));
        Map<T, Double> normalizedDistanceMap = normalizeMap(mapDistance, boundry);
        double sum = getSum(source, s -> normalizedDistanceMap.get(s));
        return collectMap(source, s -> normalizedDistanceMap.get(s) / sum);
    }

    public Map<T, Double> rankedDistance(List<T> source, List<T> next, double pc) {
        Map<T, Double> mapDistance = mapDistance(source, next);
        sort(source, mapComperator(mapDistance));
        return collectMap(source, new RankedFunction(source, pc));
    }

    private Comparator<T> mapComperator(Map<T, Double> map) {
        return (T l, T r) -> {
            Double a = map.get(l);
            Double b = map.get(r);
            if (a > b)
                return -1;
            if (a < b)
                return 1;
            return 0;
        };
    }

    public Map<T, Double> rouletteFitnessDistance(List<T> source, List<T> next) {
        Map<T, Double> mapFitness = mapFitness(source);
        Map<T, Double> mapFitnessDistance = mapFitnessDistance(source, next, mapFitness);
        Boundry boundry = getBoundry(source, s -> mapFitnessDistance.get(s));
        Map<T, Double> normalizedFitnessDistanceMap = normalizeMap(mapFitnessDistance, boundry);
        double dum = getSum(source, s -> normalizedFitnessDistanceMap.get(s));
        return collectMap(source, s -> normalizedFitnessDistanceMap.get(s));
    }

    public Map<T, Double> rankedFitnessDistance(List<T> source, List<T> next, double pc) {
        Map<T, Double> mapFitness = mapFitness(source);
        Map<T, Double> mapFitnessDistance = mapFitnessDistance(source, next, mapFitness);
        sort(source, (l, r) -> (int) (mapFitnessDistance.get(r) - mapFitnessDistance.get(l)));
        return collectMap(source, new RankedFunction(source, pc));
    }

    private Map<T, Double> mapDistance(List<T> source, List<T> next) {
        return collectMap(source, s -> distance(s, next));
    }

    private double distance(T s, List<T> next) {
        return next.stream().map(n -> s.partialDiversity(n)).reduce(0d, (a, b) -> a + b);
    }

    private Boundry getBoundry(List<T> list, Function<T, Double> f) {
        double min = list.stream().map(s -> f.apply(s)).reduce(POSITIVE_INFINITY, (a, b) -> min(a, b));
        double max = list.stream().map(e -> f.apply(e)).reduce(NEGATIVE_INFINITY, (a, b) -> max(a, b));
        return new Boundry(min, max);
    }

    private Double getSum(List<T> source, Function<T, Double> f) {
        return source.stream().map(e -> f.apply(e)).reduce(0d, (a, b) -> a + b);
    }

    private Map<T, Double> mapFitness(List<T> source) {
        return collectMap(source, s -> s.fitness());
    }

    private Map<T, Double> mapFitnessDistance(List<T> source, List<T> nextPool, Map<T, Double> fitness) {
        return collectMap(source, s -> fitnessDistance(nextPool, fitness, fitness.get(s)));
    }

    private double fitnessDistance(List<T> nextPool, Map<T, Double> fitness, double fs) {
        return nextPool.stream().map(n -> abs(fs - fitness.get(n))).reduce(0d, (a, b) -> a + b);
    }

    private Map<T, Double> normalizeMap(Map<T, Double> map, Boundry boundry) {
        return map.entrySet().stream()
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), boundry.normalize(e.getValue())), (x, y) -> x.putAll(y));
    }

    private Map<T, Double> collectMap(List<T> list, Function<T, Double> f) {
        return list.stream()
                .collect(HashMap::new, (map, e) -> map.put(e, f.apply(e)), (x, y) -> x.putAll(y));
    }

    private class RankedFunction
            implements Function<T, Double> {

        private final List<T> source;
        private final double pc;
        private int i = -1;
        private final int last;

        RankedFunction(List<T> source, double pc) {
            this.source = source;
            this.pc = pc;
            last = source.size() - 1;
        }

        @Override
        public Double apply(T e) {
            ++i;
            return i < last ? pow(1 - pc, i) * pc : pow(1 - pc, i);
        }
    }
}
