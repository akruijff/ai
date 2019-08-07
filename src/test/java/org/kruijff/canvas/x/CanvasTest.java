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

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import static java.util.Arrays.setAll;
import java.util.function.IntUnaryOperator;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import static org.awaitility.Awaitility.await;
import org.hamcrest.core.IsNot;
import org.hamcrest.text.IsEmptyString;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;
import static org.kruijff.canvas.Canvas.color;
import org.kruijff.canvas.exceptions.CanvasDimensionException;
import static org.kruijff.utilities.swing.SwingUtil.fetchChildNamed;

public class CanvasTest {

    private CanvasFrame frame;
    private Canvas canvas;

    @Before
    public void setup() {
        frame = new CanvasFrame();
        canvas = frame.canvas();
    }

    @After
    public void teardown() {
        frame.setVisible(false);
        frame = null;
    }

    private static Component[] createComponents(String... names) {
        Component[] components = new Component[names.length];
        for (int i = 0; i < names.length; ++i) {
            components[i] = new JSlider();
            components[i].setName(names[i]);
        }
        return components;
    }

    @Test(timeout = 1000)
    public void mouseAndStatusBar() {
        JLabel statusLabel = fetchChildNamed(frame, "status", JLabel.class);
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                canvas.setStatus("(" + e.getX() + ", " + e.getY() + ')');
            }
        });
        frame.dispatchEvent(new MouseEvent(frame, MouseEvent.MOUSE_MOVED, 0, 0, 150, 250, 0, false));
        assertNotNull(await().until(statusLabel::getText, new IsNot<>(new IsEmptyString())));
    }

    @Test
    public void addControl() {
        Component[] expected = createComponents("slider1", "slider2");
        frame.addControl(expected[0]);
        frame.addControl(expected[1]);
        JPanel controls = fetchChildNamed(frame, "controls", JPanel.class);
        Component[] components = controls.getComponents();
        assertSame(expected[0], components[0]);
        assertSame(expected[1], components[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addControl_WrongIndex() {
        Component[] expected = createComponents("slider1", "slider2");
        frame.addControl(expected[0], 1);
    }

    @Test
    public void addControl_Index() {
        Component[] expected = createComponents("slider1", "slider2");
        frame.addControl(expected[0]);
        frame.addControl(expected[1], 0);
        JPanel controls = fetchChildNamed(frame, "controls", JPanel.class);
        Component[] components = controls.getComponents();
        assertSame(expected[0], components[1]);
        assertSame(expected[1], components[0]);
    }

    @Test
    public void addControl_LastIndex() {
        Component[] expected = createComponents("slider1", "slider2");
        frame.addControl(expected[0]);
        frame.addControl(expected[1], 1);
        JPanel controls = fetchChildNamed(frame, "controls", JPanel.class);
        Component[] components = controls.getComponents();
        assertSame(expected[0], components[0]);
        assertSame(expected[1], components[1]);
    }

    @Test(expected = CanvasDimensionException.class)
    public void canvas_uploadPixels_ArrayWrongSize() {
        int[] arr = new int[0];
        canvas.updatePixels(arr);
    }

    @Test
    public void color_int() {
        int shade = 128;
        int expected = (shade << 16) + (shade << 8) + shade;
        int actual = color(shade);
        assertEquals(expected, actual);
    }

    @Test
    public void color_int_int() {
        int shade = 128;
        int expected = (255 << 24) + (shade << 16) + (shade << 8) + shade;
        int actual = color(128, 255);
        assertEquals(expected, actual);
    }

    @Test
    public void color_int_int_int() {
        int expected = (192 << 16) + (128 << 8) + 64;
        int actual = color(192, 128, 64);
        assertEquals(expected, actual);
    }

    @Test
    public void color_int_int_int_int() {
        int expected = (255 << 24) + (192 << 16) + (128 << 8) + 64;
        int actual = color(192, 128, 64, 255);
        assertEquals(expected, actual);
    }

    @Test
    public void canvas_uploadPixels() {
        int[] actual = setPixels(i -> i);
        canvas.updatePixels(actual);
        assertPixelsEquals(actual, canvas.loadPixels());
    }

    @Test
    public void background() {
        int c = 255 * 16 + 255 * 8 + 255;
        int[] expected = setPixels(i -> c);
        canvas.background(c);
        assertPixelsEquals(expected, canvas.loadPixels());
    }

    private int[] setPixels(IntUnaryOperator generator) {
        int[] actual = new int[canvas.getWidth() * canvas.getHeight()];
        setAll(actual, generator);
        return actual;
    }

    private void assertPixelsEquals(int[] expected, int[] actual) {
        setAll(actual, i -> actual[i] & mask(0, 255, 255, 255));
        assertArrayEquals(expected, actual);
    }

    private static int mask(int a, int r, int g, int b) {
        return (a << 24) + (r << 16) + (g << 8) + b;
    }
}
