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
package org.kruijff.canvas;

import java.awt.Color;

public class Canvas {

    private static final long serialVersionUID = 1L;

    private final CanvasImage img;
    private final CanvasFrame frame;

    public static int color(int gray) {
        return color(gray, 0);
    }

    public static int color(int gray, int alpha) {
        return color(gray, gray, gray, alpha);
    }

    public static int color(int r, int g, int b) {
        return color(r, g, b, 0);
    }

    public static int color(int r, int g, int b, int alpha) {
        return (alpha << 24) + (r << 16) + (g << 8) + b;
    }

    public Canvas(int width, int height) {
        img = new CanvasImage(width, height);
        background(color(0));

        frame = new CanvasFrame();
        frame.add(img);
        frame.setVisible(true);
        frame.pack();
    }

    public void status(String str) {
        frame.status(str);
    }

    public int[] loadPixels() {
        return img.loadPixels();
    }

    public void updatePixels(int[] pixels) {
        img.updatePixels(pixels);
    }

    private void background(int c) {
        img.background(c);
    }

    public void noFill() {
        img.noFill();
    }

    public void fill(int c) {
        img.fill(c);
    }

    public void noStroke() {
        img.noStroke();
    }

    public void stroke(int c) {
        img.stroke(c);
    }

    public void circle(int x, int y, int r) {
        img.circle(x, y, r);
    }

    private void point(int x, int y, int c) {
        img.point(x, y, c);
    }

    public int width() {
        return img.getWidth();
    }

    public int height() {
        return img.getHeight();
    }

    public void close() {
    }

    public void repaint() {
        img.invalidate();
        img.repaint();
    }
}
