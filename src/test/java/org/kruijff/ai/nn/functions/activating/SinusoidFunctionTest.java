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
package org.kruijff.ai.nn.functions.activating;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.kruijff.ai.nn.functions.ActivationFunction;

public class SinusoidFunctionTest {

    private ActivationFunction func;

    @Before
    public void setup() {
        func = new SinusoidFunction();
    }

    @After
    public void teardown() {
        func = null;
    }

    @Test
    public void apply_negative() {
        double result = func.apply(-1);
        assertEquals(-0.841, result, 0.001);
    }

    @Test
    public void apply_zero() {
        double result = func.apply(0);
        assertEquals(0, result, 0.001);
    }

    @Test
    public void apply_positive() {
        double result = func.apply(1);
        assertEquals(0.841, result, 0.001);
    }

    @Test
    public void derivative_negative() {
        double result = func.derivative(-1);
        assertEquals(0.540, result, 0.001);
    }

    @Test
    public void derivative_zero() {
        double result = func.derivative(0);
        assertEquals(1, result, 0.001);
    }

    @Test
    public void derivative_positive() {
        double result = func.derivative(1);
        assertEquals(0.540, result, 0.001);
    }
}
