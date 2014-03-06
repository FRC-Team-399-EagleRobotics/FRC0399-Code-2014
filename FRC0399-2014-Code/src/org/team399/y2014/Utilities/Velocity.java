/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.Utilities;

/**
 * Calculates velocity given a changing position.
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class Velocity {

    private double currPos = 0.0;
    private double prevPos = 0.0;
    private double vel = 0.0;

    private long prevTime = 0;
    private double period = 0.0;
    private double lastPollTime = 0.0;

    /**
     * Constructor
     */
    public Velocity() {
    }

    /**
     * Calculates velocity based on current and previous positions.
     *
     * @param pos Current sensor position for calculations.
     */
    public void run(double pos) {

        prevPos = currPos;
        this.currPos = pos;
        vel = (currPos - prevPos) / (prevTime - System.currentTimeMillis());
        prevTime = System.currentTimeMillis();
    }

    /**
     * Returns the calculated velocity.
     *
     * @return
     */
    public double getVelocity() {
        return vel;
    }
}
