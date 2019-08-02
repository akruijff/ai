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

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.kruijff.ai.ga.Chromosome;

public class TupelTest {

    @Test
    @SuppressWarnings("unchecked")
    public void compare() {
        helper(0, 0, 0, 0, 0);
        helper(0, 0, 0, 1, 1);
        helper(0, 0, 1, 0, 1);
        helper(0, 0, 1, 1, 1);

        helper(0, 1, 0, 0, -1);
        helper(0, 1, 0, 1, 0);
        helper(0, 1, 1, 0, 0);
        helper(0, 1, 1, 1, 1);

        helper(1, 0, 0, 0, -1);
        helper(1, 0, 0, 1, 0);
        helper(1, 0, 1, 0, 0);
        helper(1, 0, 1, 1, 1);

        helper(1, 1, 0, 0, -1);
        helper(1, 1, 0, 1, -1);
        helper(1, 1, 1, 0, -1);
        helper(1, 1, 1, 1, 0);
    }

    private void helper(double x1, double y1, double x2, double y2, double result) {
        Chromosome c = new ChromosomeDummy();
        Tupel<Chromosome> xx = new Tupel<>(c, x1, y1);
        Tupel<Chromosome> yy = new Tupel<>(c, x2, y2);
        assertEquals(result, xx.compareTo(yy), 0.01);
    }

}
