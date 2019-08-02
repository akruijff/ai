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

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.String.format;
import org.kruijff.ai.ga.Chromosome;
import org.kruijff.ai.ga.ID;

public class TestPointChromosome
        implements Chromosome {

    private ID id = new ID();
    private double x;
    private double y;
    private double f;

    public TestPointChromosome(int x, int y) {
        this(x, y, 0);
    }

    public TestPointChromosome(int x, int y, int f) {
        this.x = x;
        this.y = y;
        this.f = f;
    }

    @Override
    public String toString() {
        return format("id=%s, x=%.2f, y=%.2f, f=%.2f", id, x, y , f);
    }

    @Override
    public double fitness() {
        return f;
    }

    @Override
    public double partialDiversity(Chromosome obj) {
        if (this == obj)
            return 0;
        if (obj == null)
            return NEGATIVE_INFINITY;
        if (getClass() != obj.getClass())
            return POSITIVE_INFINITY;
        return distance(obj);
    }

    private double distance(Chromosome obj) {
        return distance(((TestPointChromosome) obj).x, ((TestPointChromosome) obj).y);
    }

    private double distance(double ox, double oy) {
        return sqrt(pow(x - ox, 2) + pow(y - oy, 2));
    }
}
