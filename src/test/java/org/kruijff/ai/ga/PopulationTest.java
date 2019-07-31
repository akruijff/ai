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
import java.util.function.Function;
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
        settings.setSelectionSize(8);
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
    public void selection_loop() {
        settings.setMutationChance(0);
        settings.selectFunc = list -> list.get(0);
        settings.crossoverFunc = (left, rigth) -> left;
        Population<ID> p1 = new Population<>(settings).init();
        Population<ID> p2 = p1.evolution(stop);
        assertEquals(settings.poolSize, p1.size());
        assertEquals(settings.poolSize, p2.size());
        assertPopulationContains(p2, new ID(1));
        for (int i = 1; i < settings.poolSize; ++i)
            assertPopulationContains(p2, new ID(i + settings.poolSize));
    }

    @Test
    public void crossover_loop() {
        settings.setSelectionSize(3);
        settings.setMutationChance(0);
        settings.selectFunc = new SelectNthElementFunction();
        settings.crossoverFunc = (left, rigth) -> left;
        Population<ID> p1 = new Population<>(settings).init();
        Population<ID> p2 = p1.evolution(stop);
        assertEquals(settings.poolSize, p1.size());
        assertEquals(settings.poolSize, p2.size());
        for (int i = 0; i < settings.selectionSize; ++i)
            assertPopulationContains(p2, new ID(i + 1));
        for (int i = settings.selectionSize; i < settings.poolSize; ++i)
            assertPopulationContains(p2, new ID(i + 1 + settings.poolSize - settings.selectionSize));
    }

    @Test
    public void crossover() {
        settings.setSelectionSize(3);
        settings.setMutationChance(0);
        settings.selectFunc = new SelectNthElementFunction();
        settings.crossoverFunc = (left, rigth) -> new ID();
        Population<ID> p1 = new Population<>(settings).init();
        Population<ID> p2 = p1.evolution(stop);
        assertEquals(settings.poolSize, p1.size());
        assertEquals(settings.poolSize, p2.size());
        for (int i = 1; i < settings.selectionSize; ++i)
            assertPopulationContains(p2, new ID(i + 1));
        for (int i = settings.selectionSize; i < settings.selectionSize; ++i)
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

    /**
     * This function returns the nth element when it is called the nth time.
     */
    public static class SelectNthElementFunction
            implements Function<List<ID>, ID> {

        private int index = -1;

        @Override
        public ID apply(List<ID> list) {
            ++index;
            if (index >= list.size())
                index = 0;
            return list.get(index);
        }
    }
}
