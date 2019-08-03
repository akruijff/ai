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

public class HillClimbingCanvas
        extends Canvas
        implements PopulationListener<PointChromosome> {

    private static final int W = 4;
    private static final int H = 4;
    private static final int R = 8;
    private static final int ELITE_SELECTED_COLOR = color(255, 0,0);
    private static final int SELECTED_COLOR = color(0, 255, 0);
    private static final int MUTATED_COLOR = color(255, 0, 255);
    private static final int CROSSOVER_COLOR = color(0, 0, 255);

    public HillClimbingCanvas(int width, int height) {
        super(width, height);
    }

    @Override
    public void initialPopulation(Population<PointChromosome> p) {
    }

    @Override
    public void startEvolvingPopulation(Population<PointChromosome> p) {
        drawBackground();
    }

    void drawBackground() {
        int pixels[] = loadPixels();
        for (int x = 0; x < width(); ++x)
            for (int y = 0; y < height(); ++y) {
                double xx = 4d * x / width();
                double yy = 4d * (height() - y) / height();
                int f = (int) PointChromosome.fitness(xx, yy);
                int c = Canvas.color(f % 256);
                pixels[x + width() * y] = c;
            }
        updatePixels(pixels);
    }

    @Override
    public void eliteSelectedChromosome(PointChromosome c) {
        fill(ELITE_SELECTED_COLOR);
        noStroke();
        drawChromsome(c);
    }

    @Override
    public void selectedChromosome(PointChromosome c) {
        fill(SELECTED_COLOR);
        noStroke();
        drawChromsome(c);
    }

    @Override
    public void mutatedChromosome(PointChromosome c) {
        fill(MUTATED_COLOR);
        noStroke();
        drawChromsome(c);
    }

    @Override
    public void crossoverChromosome(PointChromosome c) {
        fill(CROSSOVER_COLOR);
        noStroke();
        drawChromsome(c);
    }

    private void drawChromsome(PointChromosome c) {
        int x = (int) (width() * c.x / W);
        int y = (int) (height() * (H - c.y) / H);
        circle(x, y, R);
    }

    @Override
    public void endEvolvingPopulation(Population<PointChromosome> p) {
        repaint();
    }
}
