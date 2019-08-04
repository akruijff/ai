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

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.random;
import static java.lang.Math.sin;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.kruijff.ai.ga.Population;
import org.kruijff.ai.ga.Settings;
import org.kruijff.ai.ga.fitness.ChanceUtil;
import org.kruijff.ai.ga.fitness.SelectionFunction;
import org.kruijff.ai.ga.stop.MaxEvolutionStopCondition;

public class MainGA {

    private static final double STEP_SIZE = .1;
    private static final double PC = .05;

    public static void main(String[] args) {
        BiFunction<Double, Double, Double> sum;
        BiFunction<List<PointChromosome>, List<PointChromosome>, Map<PointChromosome, Double>> func1;
        BiFunction<List<PointChromosome>, List<PointChromosome>, Map<PointChromosome, Double>> func2;
        BiFunction<List<PointChromosome>, List<PointChromosome>, Map<PointChromosome, Double>> mapFunction;

        sum = new BiFunction<Double, Double, Double>() {
            @Override
            public Double apply(Double t, Double u) {
                return (t + u) / 2;
            }
        };
        func1 = (source, next) -> new ChanceUtil<PointChromosome>().rankedFitness(source, PC);
        func2 = (source, next) -> new ChanceUtil<PointChromosome>().rankedDistance(source, next, PC);
        mapFunction = new ChanceUtil<PointChromosome>().combineMaps(func1, func2, sum);

        HillClimbingCanvas canvas = new HillClimbingCanvas(1024, 768);
        canvas.drawBackground();

        Settings<PointChromosome> settings = new Settings<>();
        settings.setPoolSize(64);
//        settings.setEliteSize(6);
        settings.setMutationChance(.05);

        // @TODO Test for functions set
        // @TODO Test for functions returns null
        settings.setInitFunction(() -> new PointChromosome(.1 + random() / 10, .1 + random() / 10));
        settings.setSelectFunction(new SelectionFunction<>(mapFunction));
        settings.setCrossoverFunction((left, rigth) -> random() < .5
                ? new PointChromosome(left.x, rigth.y)
                : new PointChromosome(rigth.x, left.y));
        settings.setMutationFunction((p, c) -> mutate(c));

        Population<PointChromosome> p = new Population<>(settings);
        p.addPopulationListener(canvas);
        p.init();

        Population<PointChromosome> best = p.evolution(new MaxEvolutionStopCondition<>(20000));
        System.out.println("Evolution count: " + settings.getEvolutionCount());
        System.out.println("Best chromosone: " + best.getBest());
        canvas.close();
    }

    private static PointChromosome mutate(PointChromosome c) {
        double r = random() - .5;
        double step = 2 * STEP_SIZE * r;
        double a = 2 * PI * random();
        double x = c.x + step * sin(a);
        double y = c.y + step * cos(a);
        return new PointChromosome(x, y);
    }
}
