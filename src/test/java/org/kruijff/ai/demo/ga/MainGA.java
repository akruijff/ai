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
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.kruijff.ai.ga.Population;
import org.kruijff.ai.ga.Settings;
import org.kruijff.ai.ga.stop.MaxEvolutionStopCondition;

public class MainGA {

    private static final double STEP_SIZE = 0.1;

    public static void main(String[] args) {
        HillClimbingCanvas canvas = new HillClimbingCanvas(640, 480);
        canvas.drawBackground();

        Settings<Chromosome> settings = new Settings<>();
        // @TODO Test for functions set
        // @TODO Test for functions returns null
        settings.setInitFunction(() -> new Chromosome(random() / 10, random() / 10));
        settings.setSelectFunction(new SimpleFitnessFunction());
        settings.setCrossoverFunction((left, rigth) -> new Chromosome(left.x, rigth.y));
        settings.setMutationFunction((p, c) -> c.mutate(STEP_SIZE));

        Population<Chromosome> p = new Population<>(settings);
        p.addPopulationListener(canvas);
        p.init();

        /*
         * The algorithm terminates if the population has converged (does not produce offspring which
         * are significantly different from the previous generation). Then it is said that the genetic
         * algorithm has provided a set of solutions to our problem.
         */
        Population<Chromosome> best = p.evolution(new MaxEvolutionStopCondition<>(10));
        System.out.println("Evolution count: " + settings.getEvolutionCount());
        System.out.println("Best chromosone: " + best.getBest());
        canvas.close();
    }

    public static class SimpleFitnessFunction
            implements Function<List<Chromosome>, Chromosome> {

        @Override
        public Chromosome apply(List<Chromosome> list) {
            return selectChromosomeOrNull(list);
        }

        private Chromosome selectChromosomeOrNull(List<Chromosome> list) {
            double sum = getSumOfPostiveFitnessValues(list);
            return list.stream().filter(c -> isChromosomeSelected(c, sum)).findFirst().orElse(null);
        }

        private Double getSumOfPostiveFitnessValues(List<Chromosome> list) {
            return list.stream()
                    .map(c -> c.fitness())
                    .map(f -> f != null && f > 0 ? f : 0)
                    .reduce(0d, (a, b) -> a + b);
        }

        private boolean isChromosomeSelected(Chromosome c, double sum) {
            double chance = c.fitness() / sum;
            return chance < random();
        }
    }
}
