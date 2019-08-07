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
package org.kruijff.canvas.x;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import org.kruijff.canvas.exceptions.CanvasDimensionException;

public class Canvas {

    private final CanvasComponent comp;

    Canvas(CanvasComponent comp) {
        this.comp = comp;
    }

    public int getWidth() {
        return comp.getImage().getWidth();
    }

    public int getHeight() {
        return comp.getImage().getHeight();
    }

    public int[] loadPixels() {
        final BufferedImage img = comp.getImage();
        return img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
    }

    public void updatePixels(int[] pixels) {
        BufferedImage img = comp.getImage();
        if (pixels.length != img.getWidth() * img.getHeight())
            throw new CanvasDimensionException(img.getWidth(), img.getHeight());
        img.setRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
    }

    public void background(int c) {
        BufferedImage img = comp.getImage();
        int width = img.getWidth(), height = img.getHeight();
        int[] pixels = new int[width * height];
        Arrays.fill(pixels, c);
        img.setRGB(0, 0, width, height, pixels, 0, width);
    }
}
