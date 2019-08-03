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

import static java.lang.Math.ceil;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Settings<T extends Chromosome> {

    int poolSize = 128;
    int eliteSize = 2;
    double mutationChance = 0.01;
    double crossoverPercentage = 0.25;
    int evolutionCount = 0;
    Supplier<T> initFunc;
    BiFunction<List<T>, List<T>, T> selectFunc;
    BiFunction<T, T, T> crossoverFunc;
    BiConsumer<Population<T>, T> mutationFunc;

    public int getEvolutionCount() {
        return evolutionCount;
    }

    public void setPoolSize(int size) {
        poolSize = size;
        if (eliteSize > size)
            eliteSize = size;
    }

    public void setEliteSize(int size) {
        if (poolSize < size)
            poolSize = size;
        eliteSize = size;
    }

    public void setMutationChance(double chance) {
        mutationChance = chance;
    }

    public void setCrossoverPercentage(double percentage) {
        crossoverPercentage = percentage;
    }

    int crossoverNumber() {
        return (int) ceil(poolSize * crossoverPercentage);
    }

    public void setInitFunction(Supplier<T> f) {
        this.initFunc = f;
    }

    public void setSelectFunction(BiFunction<List<T>, List<T>, T> f) {
        this.selectFunc = f;
    }

    public void setCrossoverFunction(BiFunction<T, T, T> f) {
        this.crossoverFunc = f;
    }

    public void setMutationFunction(BiConsumer<Population<T>, T> f) {
        this.mutationFunc = f;
    }
}
