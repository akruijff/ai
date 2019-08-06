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

import static java.util.Arrays.setAll;
import javax.swing.JFrame;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.kruijff.canvas.exceptions.CanvasDimensionException;

public class CanvasTest {

    private CanvasFrame frame;
    private Canvas canvas;

    @Before
    public void setup() {
        frame = new CanvasFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        canvas = frame.canvas();
    }

    @After
    public void teardown() {
        frame.setVisible(false);
        frame = null;
    }

    @Test(expected = CanvasDimensionException.class)
    public void canvas_uploadPixels_ArrayWrongSize() {
        int[] arr = new int[0];
        canvas.updatePixels(arr);
    }

    @Test
    public void canvas_uploadPixels() {
        int n = canvas.getWidth() * canvas.getHeight();
        int[] arr = new int[n];
        setAll(arr, i -> i);

        canvas.updatePixels(arr);

        int[] pixels = canvas.loadPixels();
        setAll(pixels, i -> pixels[i] & mask(0, 255, 255, 255));
        assertArrayEquals(arr, pixels);
    }

    private static int clip(final int s, int mask) {
        return s & mask;
    }

    private static int mask(int a, int r, int g, int b) {
        return (a << 24) + (r << 16) + (g << 8) + b;
    }
//    @Test
//    public void test() {
//        CanvasFrame frame = new CanvasFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//
//        CanvasComponent canvas = SwingUtil.fetchChildNamed(frame, "canvas", CanvasComponent.class);
//        JLabel status = SwingUtil.fetchChildNamed(frame, "status", JLabel.class);
//        frame.addMouseMotionListener(new MouseMotionListener() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                status.setText(e.toString());
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                status.setText(e.toString());
//            }
//        });
//        MouseEvent e = new MouseEvent(status,
//                                      MouseEvent.MOUSE_MOVED,
//                                      System.currentTimeMillis() + 10,
//                                      0,
//                                      200, 201,
//                                      0, false);
//        invokeLater(() -> frame.dispatchEvent(e));
//        canvas.dispatchEvent(e);
//        frame.dispatchEvent(e);
//    }
}
