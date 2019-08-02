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
import org.junit.Before;
import org.junit.Test;
import org.kruijff.ai.ga.Chromosome;

public class TupalListBuilderTest {

    private List<Chromosome> sourcePool;
    private List<Chromosome> nextPool;

    @Before
    public void setup() {
        sourcePool = new ArrayList<>();
        nextPool = new ArrayList<>();
    }

    @After
    public void teardown() {
        sourcePool = null;
        nextPool = null;
    }

    @Test
    public void test() {
        Chromosome e1 = new TestPointChromosome(0, 0);
        sourcePool.add(e1);
        TupelListBuilder<Chromosome> builder = new TupelListBuilder<>(sourcePool, nextPool);
        List<Tupel<Chromosome>> list = builder.createTupelList();
        assertEquals(1, list.size());
        assertEquals(e1, list.get(0).chromosome());
    }

    @Test
    public void test2() {
        Chromosome e1 = new TestPointChromosome(0, 0);
        Chromosome e2 = new TestPointChromosome(3, 4, 1);
        sourcePool.add(e1);
        sourcePool.add(e2);
        TupelListBuilder<Chromosome> builder = new TupelListBuilder<>(sourcePool, nextPool);
        List<Tupel<Chromosome>> list = builder.createTupelList();
        assertEquals(2, list.size());
        assertEquals(e2, list.get(0).chromosome());
        assertEquals(e1, list.get(1).chromosome());
    }

    @Test
    public void test3() {
        Chromosome e1 = new TestPointChromosome(0, 0);
        Chromosome e2 = new TestPointChromosome(3, 4, 1);
        sourcePool.add(e2);
        sourcePool.add(e1);
        TupelListBuilder<Chromosome> builder = new TupelListBuilder<>(sourcePool, nextPool);
        List<Tupel<Chromosome>> list = builder.createTupelList();
        assertEquals(2, list.size());
        assertEquals(e2, list.get(0).chromosome());
        assertEquals(e1, list.get(1).chromosome());
    }

    @Test
    public void test4() {
        Chromosome e1 = new TestPointChromosome(0, 0);
        Chromosome e2 = new TestPointChromosome(3, 4);
        Chromosome e3 = new TestPointChromosome(6, 8);
        sourcePool.add(e2);
        sourcePool.add(e1);
        sourcePool.add(e3);
        nextPool.add(e3);
        TupelListBuilder<Chromosome> builder = new TupelListBuilder<>(sourcePool, nextPool);
        List<Tupel<Chromosome>> list = builder.createTupelList();
        assertEquals(2, list.size());
        assertEquals(e1, list.get(0).chromosome());
        assertEquals(e2, list.get(1).chromosome());
    }

    @Test
    public void test5() {
        Chromosome e1 = new TestPointChromosome(0, 0, 1);
        Chromosome e2 = new TestPointChromosome(3, 4, 0);
        Chromosome e3 = new TestPointChromosome(6, 8, 0);
        sourcePool.add(e1);
        sourcePool.add(e2);
        sourcePool.add(e3);
        nextPool.add(e3);
        TupelListBuilder<Chromosome> builder = new TupelListBuilder<>(sourcePool, nextPool);
        List<Tupel<Chromosome>> list = builder.createTupelList();
        assertEquals(2, list.size());
        assertEquals(e1, list.get(0).chromosome());
        assertEquals(e2, list.get(1).chromosome());
    }

    @Test
    public void test6() {
        Chromosome e1 = new TestPointChromosome(0, 0, 0);
        Chromosome e2 = new TestPointChromosome(3, 4, 1);
        Chromosome e3 = new TestPointChromosome(6, 8, 1);
        sourcePool.add(e1);
        sourcePool.add(e2);
        sourcePool.add(e3);
        nextPool.add(e3);
        TupelListBuilder<Chromosome> builder = new TupelListBuilder<>(sourcePool, nextPool);
        List<Tupel<Chromosome>> list = builder.createTupelList();
        assertEquals(2, list.size());
        assertEquals(e2, list.get(0).chromosome());
        assertEquals(e1, list.get(1).chromosome());
    }
}
