/*  
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.Utilities;

/**
 * Simple utility to monitor a value and return a value at the time the hold() method was called
 * (jeremy.germita@gmail.com) Jeremy Germita
 */
public class SampleAndHold {

    private double value = 0.0;
    private boolean holding = false;

    public SampleAndHold() {
    }

    public void update(double value) {
        if (!this.holding) {
            this.value = value;
        }
    }

    public void hold(boolean holding) {
        this.holding = holding;
    }
    
    public double get() {
        return this.value;
    }
}
