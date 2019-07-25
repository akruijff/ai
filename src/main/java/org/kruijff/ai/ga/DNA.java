/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
