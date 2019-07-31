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
package org.kruijff.ai.demo;

import static java.lang.Double.doubleToLongBits;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.sin;
import static java.lang.String.format;
import org.kruijff.ai.ga.Fitness;

public class Chromosome
        implements Fitness {

    private static final double W = Math.PI;
    private static final double O = 7;
    private static final double C = 767;

    double x;
    double y;

    public static double fitness(double x, double y) {
        return C * pow(sin(W * x), 2) * pow(sin(W * y), 2) * exp((x + y) / O);
    }

    public Chromosome(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return format("Chromosome{x=%.2f, y=%.2f, fitness=%f}", x, y, fitness());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (int) (doubleToLongBits(this.x) ^ (doubleToLongBits(this.x) >>> 32));
        hash = 73 * hash + (int) (doubleToLongBits(this.y) ^ (doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && doubleToLongBits(this.x) == doubleToLongBits(((Chromosome) obj).x)
                && doubleToLongBits(this.y) == doubleToLongBits(((Chromosome) obj).y);
    }

    @Override
    public double fitness() {
        return fitness(x, y);
    }

    void mutate(double stepSize) {
        double r = Math.random() - .5;
        double step = 2 * stepSize * r;
        step(step);
    }

    private void step(double step) {
        if (random() < .5)
            this.x += step;
        else
            this.y += step;
    }
}
