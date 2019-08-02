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

import static java.lang.Math.sqrt;
import static java.lang.String.format;
import java.util.Objects;
import org.kruijff.ai.ga.Chromosome;

class Tupel<T extends Chromosome>
        implements Comparable<Tupel<T>> {

    private T chromosome;
    private double fitness;
    private double diversity;

    Tupel(T chromosome, double fitness, double divisity) {
        this.chromosome = chromosome;
        this.fitness = fitness;
        this.diversity = divisity;
    }

    @Override
    public String toString() {
        return format("weigth=%.2f, fitness=%.2f, diversity=%.2f, chromosome={%s}", weight(), fitness, diversity, chromosome);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.chromosome);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.chromosome, ((Tupel<?>) obj).chromosome);
    }

    @Override
    public int compareTo(Tupel<T> other) {
        double d = other.diff(this);
        return convert(d);
    }

    private double diff(Tupel<T> other) {
        double d = weight() - other.weight();
        if (d == 0)
            d = diversity - other.diversity;
        if (d == 0)
            d = fitness - other.fitness;
        return d;
    }

    private int convert(double d) {
        if (d < 0)
            return -1;
        else if (d > 0)
            return 1;
        else
            return 0;
    }

    private double weight() {
        return sqrt(fitness * fitness + diversity * diversity);
    }

    T chromosome() {
        return chromosome;
    }
}
