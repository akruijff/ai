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

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.kruijff.ai.ga.Chromosome;

public class FitnessAndDiversityRankedFunctionTest {

    private List<Chromosome> sourcePool;
    private List<Chromosome> nextPool;
    private FitnessAndDiversityRankedFunction<Chromosome> func;

    @Before
    public void setup() {
        sourcePool = new ArrayList<>();
        nextPool = new ArrayList<>();
        func = new FitnessAndDiversityRankedFunction<>(() -> 0);
    }

    @After
    public void teardown() {
        func = null;
    }

    @Test(expected = SourcePoolEmptyException.class)
    public void apply_EmptySourcePool_EmptyNextPool() {
        func.apply(sourcePool, nextPool);
    }

    @Test
    public void apply_OneChromosome_EmptyNextPool() {
        ChromosomeStub expected = new ChromosomeStub();
        sourcePool.add(expected);
        Chromosome actual = func.apply(sourcePool, nextPool);
        assertEquals(expected, actual);
    }

    @Test
    public void apply_TwoEqualsChromosome_EmptyNextPool() {
        ChromosomeStub e1 = new ChromosomeStub();
        ChromosomeStub e2 = new ChromosomeStub();
        sourcePool.add(e1);
        sourcePool.add(e2);
        Chromosome actual = func.apply(sourcePool, nextPool);
        assertEquals(e1, actual);
        assertEquals(e2, actual);
    }

    @Test
    public void apply_TwoChromosome_DifferentFitness_EmptyNextPool() {
        ChromosomeStub e1 = new ChromosomeStub(1, 0);
        ChromosomeStub e2 = new ChromosomeStub(0, 0);
        sourcePool.add(e1);
        sourcePool.add(e2);
        Chromosome actual = func.apply(sourcePool, nextPool);
        assertEquals(e1, actual);
        assertNotEquals(e2, actual);
    }

    @Test
    public void apply_TwoChromosome_DifferentDiversity_EmptyNextPool() {
        ChromosomeStub e1 = new ChromosomeStub(0, 1);
        ChromosomeStub e2 = new ChromosomeStub(0, 0);
        sourcePool.add(e1);
        sourcePool.add(e2);
        Chromosome actual = func.apply(sourcePool, nextPool);
        assertEquals(e1, actual);
        assertNotEquals(e2, actual);
    }

    @Test
    public void apply_TwoChromosome_DifferentFitnessAndDiversity_EmptyNextPool() {
        ChromosomeStub e1 = new ChromosomeStub(0, 1);
        ChromosomeStub e2 = new ChromosomeStub(1, 0);
        sourcePool.add(e1);
        sourcePool.add(e2);
        Chromosome actual = func.apply(sourcePool, nextPool);
        assertEquals(e1, actual);
        assertNotEquals(e2, actual);
    }

    @Test
    public void apply_TwoChromosome_DifferentFitnessAndDiversity2_EmptyNextPool() {
        ChromosomeStub e1 = new ChromosomeStub(1, 0);
        ChromosomeStub e2 = new ChromosomeStub(0, 1);
        sourcePool.add(e1);
        sourcePool.add(e2);
        Chromosome actual = func.apply(sourcePool, nextPool);
        assertEquals(e1, actual);
        assertNotEquals(e2, actual);
    }

    @Test
    public void apply_ThreeChromosome_DifferentFitnessAndDiversity_OneInNextPool() {
        ChromosomeStub e1 = new ChromosomeStub(1, 0);
        ChromosomeStub e2 = new ChromosomeStub(0, 1);
        ChromosomeStub e3 = new ChromosomeStub(1, 1);
        sourcePool.add(e1);
        sourcePool.add(e2);
        sourcePool.add(e3);
        nextPool.add(e3);
        Chromosome actual = func.apply(sourcePool, nextPool);
        assertNotEquals(e1, actual);
        assertEquals(e2, actual);
        assertNotEquals(e3, actual);
    }
}
