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

import org.kruijff.ai.ga.Population;
import org.kruijff.ai.ga.PopulationListener;
import org.kruijff.canvas.Canvas;
import static org.kruijff.canvas.Canvas.color;
import org.kruijff.canvas.CanvasFrame;

public class HillClimbingDrawer
        implements PopulationListener<PointChromosome> {

    private static final int W = 4;
    private static final int H = 4;
    private static final int R = 8;
    private static final int ELITE_SELECTED_COLOR = color(255, 0, 0);
    private static final int SELECTED_COLOR = color(0, 255, 0);
    private static final int MUTATED_COLOR = color(255, 0, 255);
    private static final int CROSSOVER_COLOR = color(0, 0, 255);
    private final CanvasFrame frame;
    private final Canvas canvas;

    public HillClimbingDrawer(CanvasFrame frame) {
        this.frame = frame;
        this.canvas = frame.canvas();
    }

    @Override
    public void initialPopulation(Population<PointChromosome> p) {
    }

    @Override
    public void startEvolvingPopulation(Population<PointChromosome> p) {
        drawBackground();
    }

    void drawBackground() {
        int pixels[] = canvas.loadPixels();
        for (int x = 0; x < canvas.getWidth(); ++x)
            for (int y = 0; y < canvas.getHeight(); ++y) {
                double xx = 4d * x / canvas.getWidth();
                double yy = 4d * (canvas.getHeight() - y) / canvas.getHeight();
                int f = (int) PointChromosome.fitness(xx, yy);
                int c = Canvas.color(f % 256);
                pixels[x + canvas.getWidth() * y] = c;
            }
        canvas.updatePixels(pixels);
    }

    @Override
    public void eliteSelectedChromosome(PointChromosome c) {
        canvas.fill(ELITE_SELECTED_COLOR);
        canvas.noStroke();
        drawChromsome(c);
    }

    @Override
    public void selectedChromosome(PointChromosome c) {
        canvas.fill(SELECTED_COLOR);
        canvas.noStroke();
        drawChromsome(c);
    }

    @Override
    public void mutatedChromosome(PointChromosome c) {
        canvas.fill(MUTATED_COLOR);
        canvas.noStroke();
        drawChromsome(c);
    }

    @Override
    public void crossoverChromosome(PointChromosome c) {
        canvas.fill(CROSSOVER_COLOR);
        canvas.noStroke();
        drawChromsome(c);
    }

    private void drawChromsome(PointChromosome c) {
        int x = (int) (canvas.getWidth() * c.x / W);
        int y = (int) (canvas.getHeight() * (H - c.y) / H);
        canvas.circle(x, y, R);
    }

    @Override
    public void endEvolvingPopulation(Population<PointChromosome> p) {
        frame.repaintCanvas();
    }
}
