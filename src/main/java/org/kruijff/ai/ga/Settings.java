/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.ga;

import java.util.function.Function;
import java.util.function.Supplier;

class Settings {

    int capacity;
    int keepCount;
    double selectionChange = 0.2;
    Function<DNA, Integer> fitnesse;
    double mutationChance = 0.01;
    double addMutationChance = 0.001;
    double removeMutationChance = 0.001;

    Settings(int capacity) {
        this.capacity = capacity;
    }

    Supplier<Gene> geneSupplier() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean isAddGene() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean isRemoveGene() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean isMutateGene() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
