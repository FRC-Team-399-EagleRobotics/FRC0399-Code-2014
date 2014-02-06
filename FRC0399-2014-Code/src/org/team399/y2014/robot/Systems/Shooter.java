/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Talon;
import org.team399.y2014.Utilities.EagleMath;
import org.team399.y2014.robot.Config.Constants;

/**
 * Shooter class. Base functions for shooter mechanism
 *
 * @author
 */
public class Shooter {

    private Talon m_shooterA = null;    // Motor Controllers
    private Talon m_shooterB = null;

    private AnalogChannel m_pot = null; // Position sensor

    private double goal = 0;

    /**
     * Constructor
     *
     * @param a motor controller A port
     * @param b motor controller B port
     * @param p potentiometer/position sensor port.
     */
    public Shooter(int a, int b, int p) {
        m_shooterA = new Talon(a);
        m_shooterB = new Talon(b);
        m_pot = new AnalogChannel(p);
    }

    /**
     * Gets position sensor value.
     *
     * @return Potentiometer value
     */
    public double getPosition() {
        double answer = m_pot.getVoltage();
        return answer;
    }

    /**
     * Sets motor controller outputs.
     *
     * @param value
     */
    private void setOutput(double value) {
        m_shooterA.set(value);
        //m_shooterB.set(value);    // Might want to negate this before enable.
    }

    /**
     * Contains state values for shooter system finite state machine.
     */
    public static class States {

        final static int STOW = 0;
        final static int SHOOT = 1;
        final static int PASS = 2;
        final static int MANUAL = 99;

    }

    private int curr_state = 0, prev_state = 0;

    /**
     * Sets a new state for the shooter finite state machine
     *
     * @param newState new state to use
     */
    public void setState(int newState) {
        prev_state = curr_state;
        curr_state = newState;
    }

    /**
     * Returns the current state of the shooter FSM
     *
     * @return
     */
    public int getState() {
        return curr_state;
    }

    /**
     * Runs the shooter finite state machine with control loops
     */
    public void run() {
        double output = 0;

        if (curr_state == States.STOW) {
            // If stow, do this
            goal = Constants.Shooter.STOW_POS;
            output = pidControl(
                    Constants.Shooter.STOW_P,
                    Constants.Shooter.STOW_I,
                    Constants.Shooter.STOW_D,
                    Constants.Shooter.STOW_F,
                    Constants.Shooter.STOW_S);
        } else if (curr_state == States.SHOOT) {
            // Else if shoot, do this
        } else if (curr_state == States.PASS) {
            // Pass do this
            goal = Constants.Shooter.PASS_POS;
            output = pidControl(
                    Constants.Shooter.PASS_P,
                    Constants.Shooter.PASS_I,
                    Constants.Shooter.PASS_D,
                    Constants.Shooter.PASS_F,
                    Constants.Shooter.PASS_S);
        } else if (curr_state == States.MANUAL) {

        } else {
            System.out.println("[SHOOTER] Invalid State!!");
        }
        this.setOutput(output);
    }

    private double error = 0, prevError = 0;
    private double intError = 0;

    /**
     * Calculates a position control output for shooter arm positioning
     *
     * @param p Proportional gain
     * @param i Integral gain
     * @param d Derivative Gain
     * @param f Feed Forward Gain
     * @param s Speed Limit
     * @return a calculated closed loop control output
     */
    private double pidControl(double p, double i, double d, double f, double s) {
        prevError = error;
        error = this.getPosition() - goal;

        intError += error;
        if (Math.abs(intError) > 0.5) {
            intError = 0.5 * EagleMath.signum(intError);
        }

        double pOut = p * error;
        double iOut = i * intError;
        double dOut = d * (error - prevError);

        double output = pOut + iOut + dOut + f;

        if (Math.abs(output) > Math.abs(s)) {
            output = Math.abs(s) * EagleMath.signum(output);
        }

        return output;
    }

}
