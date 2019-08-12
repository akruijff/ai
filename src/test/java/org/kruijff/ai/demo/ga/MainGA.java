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

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.random;
import static java.lang.Math.sin;
import static java.lang.String.format;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.swing.JSlider;
import org.kruijff.ai.ga.Chromosome;
import org.kruijff.ai.ga.Population;
import org.kruijff.ai.ga.Settings;
import org.kruijff.ai.ga.fitness.ChanceUtil;
import org.kruijff.ai.ga.fitness.SelectionFunction;
import org.kruijff.ai.ga.stop.MaxEvolutionStopCondition;
import org.kruijff.canvas.CanvasFrame;

public class MainGA {

    private static final double STEP_SIZE = .5;
    private static final double PC = .05;

    public static void main(String[] args) {
        Settings<PointChromosome> settings = getSettings();
        JSlider delaySlider = createDelaySlider();
        CanvasFrame frame = createFrame(settings, delaySlider);

        HillClimbingDrawer drawer = new HillClimbingDrawer(frame);
        Population<PointChromosome> best = buildAndEvolvePopupation(settings, drawer);
        System.out.println("Evolution count: " + settings.getEvolutionCount());
        System.out.println("Best chromosone: " + best.getBest());
    }

    private static JSlider createDelaySlider() {
        JSlider delaySlider = new JSlider(0, 2000, 500);
        delaySlider.setMajorTickSpacing(1000);
        delaySlider.setMinorTickSpacing(100);
        delaySlider.setPaintTicks(true);
        delaySlider.setPaintLabels(true);
        return delaySlider;
    }

    private static CanvasFrame createFrame(Settings settings, JSlider delaySlider) {
        CanvasFrame frame = new CanvasFrame();
        frame.addControl(delaySlider);
        frame.canvas().addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseMotion(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseMotion(e);
            }

            private void mouseMotion(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                double fitness = 0;
                int n = settings.getEvolutionCount();
                frame.canvas().setStatus(format("(%d, %d) = %.2f {%d}", x, y, fitness, n));
                frame.invalidate();
            }
        });
        frame.pack();
        frame.setVisible(true);
        return frame;
    }

    private static Settings<PointChromosome> getSettings() {
        Settings<PointChromosome> settings = new Settings<>();
        settings.setPoolSize(64);
        //        settings.setEliteSize(6);
        settings.setMutationChance(.05);
        // @TODO Test for functions set
        // @TODO Test for functions returns null
        settings.setInitFunction(() -> init());
        settings.setSelectFunction(selection());
        settings.setCrossoverFunction(crossover());
        settings.setMutationFunction(mutation());
        return settings;
    }

    private static Population<PointChromosome> buildAndEvolvePopupation(Settings<PointChromosome> settings, HillClimbingDrawer canvas) {
        Population<PointChromosome> p = new Population<>(settings);
        p.addPopulationListener(canvas);
        p.init();
        Population<PointChromosome> best = p.evolution(new MaxEvolutionStopCondition<>(20000));
        return best;
    }

    private static PointChromosome init() {
        return new PointChromosome(.1 + random() / 10, .1 + random() / 10);
    }

    private static SelectionFunction<PointChromosome> selection() {
        return new SelectionFunction<>(selectionMap());
    }

    private static <T extends Chromosome> BiFunction<List<T>, List<T>, Map<T, Double>> selectionMap() {
        BiFunction<List<T>, List<T>, Map<T, Double>> func1 = (source, next) -> new ChanceUtil<T>().rankedFitness(source, PC);
        BiFunction<List<T>, List<T>, Map<T, Double>> func2 = (source, next) -> new ChanceUtil<T>().rankedDistance(source, next, PC);
        BiFunction<Double, Double, Double> sum = (Double t, Double u) -> (t + u) / 2;
        return new ChanceUtil<T>().combineMaps(func1, func2, sum);
    }

    private static BiFunction<PointChromosome, PointChromosome, PointChromosome> crossover() {
        return (left, rigth) -> crossover(left, rigth);
    }

    private static PointChromosome crossover(PointChromosome left, PointChromosome rigth) {
        return random() < .5
                ? new PointChromosome(left.x, rigth.y)
                : new PointChromosome(rigth.x, left.y);
    }

    private static BiFunction<Population<PointChromosome>, PointChromosome, PointChromosome> mutation() {
        return (p, c) -> mutate(c);
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
