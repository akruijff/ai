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

public class TestPopulationListener
        implements PopulationListener<ID> {

    private int initial;
    private int startEvolving;
    private int selected;
    private int crossover;
    private int mutation;
    private int endEvolving;

    @Override
    public void initialPopulation(Population<ID> p) {
        ++initial;
    }

    @Override
    public void startEvolvingPopulation(Population<ID> p) {
        ++startEvolving;
    }

    @Override
    public void selectedChromosome(ID c) {
        ++selected;
    }

    @Override
    public void crossoverChromosome(ID c) {
        ++crossover;
    }

    @Override
    public void mutatedChromosome(ID c) {
        ++mutation;
    }

    @Override
    public void endEvolvingPopulation(Population<ID> p) {
        ++endEvolving;
    }

    public int initialCount() {
        return initial;
    }

    public int startEvolvingCount() {
        return startEvolving;
    }

    public int selectedCount() {
        return selected;
    }

    public int crossoverCount() {
        return crossover;
    }

    public int mutationCount() {
        return mutation;
    }

    public int endEvolvingCount() {
        return endEvolving;
    }

}
