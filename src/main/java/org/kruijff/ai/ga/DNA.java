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

import static java.lang.Integer.max;
import static java.lang.Math.floor;
import static java.lang.Math.random;
import static java.util.Arrays.copyOf;

public class DNA {

    private Gene[] genes;

    public DNA(int n) {
        genes = new Gene[n];
    }

    DNA crossover(DNA other) {
        int pivot = (int) floor(random() * genes.length);
        int n = max(genes.length, other.genes.length);
        DNA child = new DNA(n);
        for (int i = 0; i < pivot; ++i)
            child.genes[i] = genes[i];
        for (int i = pivot; i < child.genes.length; ++i)
            child.genes[i] = other.genes[i];
        return child;
    }

    void mutation(Settings settings) {
        removeGenesByChance(settings);
        doGeneMutationsByChance(settings);
        addGeneByChance(settings);
    }

    private void removeGenesByChance(Settings settings) {
        if (settings.isRemoveGene() && genes.length > 0)
            genes = copyOf(genes, genes.length - 1, Gene[].class);
    }

    private void doGeneMutationsByChance(Settings settings) {
        for (int i = 0; i < genes.length; ++i)
            if (settings.isMutateGene())
                genes[i] = settings.geneSupplier().get();
    }

    private void addGeneByChance(Settings settings) {
        if (settings.isAddGene()) {
            genes = copyOf(genes, genes.length + 1, Gene[].class);
            genes[genes.length - 1] = settings.geneSupplier().get();
        }
    }
}
