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

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.kruijff.ai.nn.functions.ActivationFunction;

public class SigmoidFunctionTest {

    @Test
    public void apply_negative_baseline() {
        ActivationFunction func = new SigmoidFunction();
        double result = func.apply(-1);
        assertEquals(.268, result, 0.001);
    }

    @Test
    public void apply_zero_baseline() {
        ActivationFunction func = new SigmoidFunction();
        double result = func.apply(0d);
        assertEquals(.5, result, 0.001);
    }

    @Test
    public void apply_positive_baseline() {
        ActivationFunction func = new SigmoidFunction();
        double result = func.apply(1);
        assertEquals(.731, result, 0.001);
    }

    @Test
    public void derivative_negative_baseline() {
        ActivationFunction func = new SigmoidFunction();
        double result = func.derivative(-1);
        assertEquals(0.196, result, 0.001);
    }

    @Test
    public void derivative_zero_baseline() {
        ActivationFunction func = new SigmoidFunction();
        double result = func.derivative(0.5d);
        assertEquals(0.235, result, 0.001);
    }

    @Test
    public void derivative_positive_baseline() {
        ActivationFunction func = new SigmoidFunction();
        double result = func.derivative(2d);
        assertEquals(0.104, result, 0.001);
    }

    @Test
    public void apply_negative_slope() {
        ActivationFunction func = new SigmoidFunction(1);
        double result = func.apply(-1);
        assertEquals(.268, result, 0.001);
    }

    @Test
    public void apply_zero_slope() {
        ActivationFunction func = new SigmoidFunction(1);
        double result = func.apply(0d);
        assertEquals(.5, result, 0.001);
    }

    @Test
    public void apply_positive_slope() {
        ActivationFunction func = new SigmoidFunction(1);
        double result = func.apply(1);
        assertEquals(.731, result, 0.001);
    }

    @Test
    public void derivative_negative_slope() {
        ActivationFunction func = new SigmoidFunction(1);
        double result = func.derivative(-1);
        assertEquals(0.196, result, .01);
    }

    @Test
    public void derivative_zero_slope() {
        ActivationFunction func = new SigmoidFunction(0d);
        double result = func.derivative(0d);
        assertEquals(0.25, result, .01);
    }

    @Test
    public void derivative_positive_slope() {
        ActivationFunction func = new SigmoidFunction(1);
        double result = func.derivative(1);
        assertEquals(0.196, result, 0.001);
    }
}
