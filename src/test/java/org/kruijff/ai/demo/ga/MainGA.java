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
package org.kruijff.ai.demo.ga;

import static java.lang.Math.random;
import org.kruijff.ai.ga.Population;
import org.kruijff.ai.ga.Settings;
import org.kruijff.ai.ga.fitness.FitnessAndDiversityRankedFunction;
import org.kruijff.ai.ga.stop.MaxEvolutionStopCondition;

public class MainGA {

    private static final double STEP_SIZE = .1;
    private static final double PC = .05;

    public static void main(String[] args) {
        HillClimbingCanvas canvas = new HillClimbingCanvas(1024, 768);
        canvas.drawBackground();

        Settings<PointChromosome> settings = new Settings<>();
        settings.setPoolSize(64);
        settings.setEliteSize(6);
        settings.setMutationChance(.05);

        // @TODO Test for functions set
        // @TODO Test for functions returns null
        settings.setInitFunction(() -> new PointChromosome(.9 + random() / 10, .9 + random() / 10));
        settings.setSelectFunction(new FitnessAndDiversityRankedFunction(PC));
        settings.setCrossoverFunction((left, rigth) -> random() < .5
                ? new PointChromosome(left.x, rigth.y)
                : new PointChromosome(left.y, rigth.x));
        settings.setMutationFunction((p, c) -> c.mutate(STEP_SIZE));

        Population<PointChromosome> p = new Population<>(settings);
        p.addPopulationListener(canvas);
        p.init();

        Population<PointChromosome> best = p.evolution(new MaxEvolutionStopCondition<>(2000));
        System.out.println("Evolution count: " + settings.getEvolutionCount());
        System.out.println("Best chromosone: " + best.getBest());
        canvas.close();
    }
}
