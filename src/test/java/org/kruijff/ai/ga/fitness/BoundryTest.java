/*
 * Copyright Aleboundry de Kruijff <aleboundry.de.kruijff@MazarineBlue.org>
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

import org.kruijff.ai.ga.fitness.x.Boundry;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BoundryTest {

    @Test
    public void normalize_10_boundry_0_20() {
        Boundry boundry = new Boundry(0, 20);
        assertEquals(.5, boundry.normalize(10), 0.01);
    }

    @Test
    public void normalize_15_boundry_0_20() {
        Boundry boundry = new Boundry(0, 20);
        assertEquals(.75, boundry.normalize(15), 0.01);
    }

    @Test
    public void normalize_20_boundry_0_20() {
        Boundry boundry = new Boundry(0, 20);
        assertEquals(1, boundry.normalize(20), 0.01);
    }

    @Test
    public void normalize_10_boundry_10_20() {
        Boundry boundry = new Boundry(10, 20);
        assertEquals(0, boundry.normalize(10), 0.01);
    }

    @Test
    public void normalize_15_boundry_10_20() {
        Boundry boundry = new Boundry(10, 20);
        assertEquals(0.5, boundry.normalize(15), 0.01);
    }

    @Test
    public void normalize_20_boundry_10_20() {
        Boundry boundry = new Boundry(10, 20);
        assertEquals(1, boundry.normalize(20), 0.01);
    }

    @Test
    public void normalize_10_boundry_10_50() {
        Boundry boundry = new Boundry(10, 50);
        assertEquals(0, boundry.normalize(10), 0.01);
    }

    @Test
    public void normalize_15_boundry_10_50() {
        Boundry boundry = new Boundry(10, 50);
        assertEquals(0.125, boundry.normalize(15), 0.01);
    }

    @Test
    public void normalize_20_boundry_10_50() {
        Boundry boundry = new Boundry(10, 50);
        assertEquals(.25, boundry.normalize(20), 0.01);
    }
}
