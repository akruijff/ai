/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronnetwork.nn.neurons;

import neuronnetwork.nn.neurons.util.NeuronVisitorSpy;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class InputNeuronTest {

    private InputNeuron n;

    @Before
    public void setup() {
        n = new InputNeuron("Test", null);
    }

    @After
    public void teardown() {
        n = null;
    }

    @Test
    public void setOutput() {
        Double expected = 1d;
        n.setOutput(expected);
        Double actual = n.output();
        assertEquals(expected, actual);
    }

    @Test
    public void visit() {
        NeuronVisitorSpy visitor = new NeuronVisitorSpy();
        n.accept(visitor);
        assertEquals(1, visitor.getCountInput());
        assertEquals(0, visitor.getCountHidden());
        assertEquals(0, visitor.getCountOutput());
    }
}
