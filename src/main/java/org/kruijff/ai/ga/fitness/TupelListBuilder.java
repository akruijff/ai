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

import org.kruijff.ai.ga.fitness.x.Boundry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kruijff.ai.ga.Chromosome;

class TupelListBuilder<T extends Chromosome> {

    private final List<T> sourcePool;
    private final List<T> nextPool;
    private final Map<T, Double> fitnessMap;
    private final Map<T, Double> diversityMap;
    private final Boundry fitnessBoundry;
    private final Boundry diversityBoundry;

    TupelListBuilder(List<T> sourcePool, List<T> nextPool) {
        this.sourcePool = sourcePool;
        this.nextPool = nextPool;
        fitnessMap = createFitnessMap(sourcePool);
        diversityMap = createDiversityMap(sourcePool);
        fitnessBoundry = createBoundry(sourcePool, fitnessMap);
        diversityBoundry = createBoundry(sourcePool, diversityMap);
    }

    //<editor-fold defaultstate="collapsed" desc="Methods for the constructor">
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
        return nextPool.stream().map((n) -> s.partialDiversity(n)).reduce(0d, (a, b) -> a + b);
    }

    private Boundry createBoundry(List<T> source, Map<T, Double> map) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (T s : source) {
            min = Math.min(min, map.get(s));
            max = Math.max(max, map.get(s));
        }
        return new Boundry(min, max);
    }
    //</editor-fold>

    List<Tupel<T>> createTupelList() {
        List<Tupel<T>> list = sourcePool.stream()
                .filter((s) -> !nextPool.contains(s))
                .map((s) -> createTupel(s))
                .collect(ArrayList::new, List::add, List::addAll);
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }

    private Tupel<T> createTupel(T s) {
        double f = 1 - fitnessBoundry.normalize(fitnessMap.get(s));
        double d = 1 - diversityBoundry.normalize(diversityMap.get(s));;
        return new Tupel<>(s, f, d);
    }
}
