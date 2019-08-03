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
package org.kruijff.ai.ga.fitness.x;

import static java.lang.Math.random;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import org.kruijff.ai.ga.Chromosome;

// https://cs.stackexchange.com/questions/69902/measuring-and-maintaining-the-diversity-of-individuals-in-genetic-algorithm
public class CombineFunction<T extends Chromosome>
        implements BiFunction<List<T>, List<T>, T> {

    private final double pc;
    private final ChanceUtil<T> util = new ChanceUtil<>();

    public CombineFunction(double pc) {
        this.pc = pc;
    }

    @Override
    public T apply(List<T> source, List<T> nextPool) {
        double r = random();
        Map<T, Double> map = util.rankedDistance(source, nextPool, pc);
        for (int i = 0; i < 2; ++i) // We migth have missed a applicatle chromosome earlier
            for (Entry<T, Double> e : map.entrySet()) {
                r -= e.getValue();
                if (r < 0 && !nextPool.contains(e.getKey()))
                    return e.getKey();
            }
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
