/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kruijff.ai.nn.fc;

public interface StopCondition {

    public boolean apply(int runs, double error);
}
