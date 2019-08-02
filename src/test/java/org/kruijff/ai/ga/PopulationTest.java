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

import java.util.List;
import java.util.function.BiFunction;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class PopulationTest {

    private Settings<ID> settings;
    private TestEvolutionStopCondition<ID> stop;

    @Before
    public void setup() {
        ID.reset();
        settings = new Settings<>();
        settings.setPoolSize(8);
        settings.setEliteSize(8);
        settings.initFunc = ID::new;
        stop = new TestEvolutionStopCondition<>(2);
    }

    @After
    public void teardown() {
        settings = null;
    }

    private static void assertPopulationContains(Population<ID> p, ID id) {
        assertTrue("Population does not contain ID: " + id, p.contains(id));
    }

    @Test
    public void init() {
        Population<ID> p = new Population<>(settings).init();
        assertEquals(settings.poolSize, p.size());
        for (int i = 0; i < settings.poolSize; ++i)
            assertPopulationContains(p, new ID(i + 1));
    }

    @Test
    public void crossover() {
        settings.setPoolSize(8);
        settings.setEliteSize(3);
        settings.setMutationChance(0);
        settings.selectFunc = new SelectNthElementFunction();
        settings.crossoverFunc = (left, rigth) -> new ID();
        Population<ID> p1 = new Population<>(settings).init();
        Population<ID> p2 = p1.evolution(stop);
        assertEquals(settings.poolSize, p1.size());
        assertEquals(settings.poolSize, p2.size());
        for (int i = 1; i < settings.eliteSize; ++i)
            assertPopulationContains(p2, new ID(i + 1));
        for (int i = settings.eliteSize; i < settings.eliteSize; ++i)
            assertPopulationContains(p2, new ID(i + 1));
    }

    @Test
    public void mutate() {
        settings.setMutationChance(1);
        settings.selectFunc = new SelectNthElementFunction();
        settings.mutationFunc = (p, id) -> id.setValue(id.getValue() + settings.poolSize);
        Population<ID> p1 = new Population<>(settings).init();
        Population<ID> p2 = p1.evolution(stop);
        assertEquals(settings.poolSize, p1.size());
        assertEquals(settings.poolSize, p2.size());
        for (int i = 0; i < settings.poolSize; ++i)
            assertPopulationContains(p2, new ID(i + 1 + settings.poolSize));
    }

    @Test
    public void populationListener() {
        int e = 3;
        settings.setEliteSize(e);
        settings.setMutationChance(1);
        settings.selectFunc = new SelectNthElementFunction();
        settings.crossoverFunc = (left, rigth) -> new ID();
        settings.mutationFunc = (p, id) -> id.setValue(id.getValue() + settings.poolSize);
        Population<ID> p = new Population<>(settings);
        TestPopulationListener l = new TestPopulationListener();
        p.addPopulationListener(l);
        p.init();
        p.evolution(stop);

        int size = p.size();
        assertEquals(1, l.initialCount());
        assertEquals(1, l.startEvolvingCount());
        assertEquals(1, l.initialCount());
        assertEquals(e, l.selectedCount());
        assertEquals(size - e, l.crossoverCount());
        assertEquals(size, l.mutationCount());
        assertEquals(1, l.endEvolvingCount());
    }

    /**
     * This function returns the nth element when it is called the nth time.
     */
    public static class SelectNthElementFunction
            implements BiFunction<List<ID>, List<ID>, ID> {

        private int index = -1;

        @Override
        public ID apply(List<ID> sourcePool, List<ID> nextPool) {
            ++index;
            if (index >= sourcePool.size())
                index = 0;
            return sourcePool.get(index);
        }
    }
}
