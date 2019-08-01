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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.util.Arrays;
import javax.swing.JLabel;

public class CanvasImage
        extends JLabel {

    private final BufferedImage img;
    private final int width;
    private final int height;
    private Color fillColor = new Color(255, 0, 0);
    private Color strokeColor = new Color(255, 255, 255);

    public CanvasImage(int width, int height) {
        img = new BufferedImage(width, height, TYPE_INT_RGB);
        this.width = width;
        this.height = height;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, this);
    }

    int[] loadPixels() {
        return img.getRGB(0, 0, width, height, null, 0, width);
    }

    void updatePixels(int[] pixels) {
        img.setRGB(0, 0, width, height, pixels, 0, width);
        invalidate();
        repaint();
    }

    void background(int c) {
        int[] pixels = new int[width * height];
        Arrays.fill(pixels, c);
        img.setRGB(0, 0, width, height, pixels, 0, width);
        invalidate();
        repaint();
    }

    void noFill() {
        fillColor = null;
    }

    void fill(int c) {
        fillColor = new Color(c);
    }

    void noStroke() {
        strokeColor = null;
    }

    void stroke(int c) {
        strokeColor = new Color(c);
    }

    void circle(int x, int y, int r) {
        Graphics g = img.getGraphics();
        if (fillColor != null) {
            g.setColor(fillColor);
            g.fillOval(x, y, r, r);
        }
        if (strokeColor != null) {
            g.setColor(strokeColor);
            g.drawOval(x, y, r, r);
        }
    }

    void point(int x, int y, int c) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return;
        if (strokeColor != null) {
            img.setRGB(x, y, strokeColor.getRGB());
            invalidate();
            repaint();
        }
    }
}
