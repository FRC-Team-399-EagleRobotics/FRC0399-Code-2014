/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import org.team399.y2014.Utilities.EagleMath;
import org.team399.y2014.robot.Config.Constants;

/**
 * Shooter class. Base functions for shooter mechanism
 *
 * @author ()
 */
public class Shooter {

    private Talon m_shooterA = null;    // Motor Controllers
    private Talon m_shooterB = null;

    private AnalogChannel m_pot = null; // Position sensor

    private double goal = 0;

    private double m_upperLim = 0.0;
    private double m_lowerLim = 5.0;
    private boolean m_limitsEnabled = false;

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
        m_pot = new AnalogChannel(1);
        
        this.setSoftLimits(Constants.Shooter.LOWER_LIMIT,Constants.Shooter.UPPER_LIMIT, true);
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
    public void setOutput(double value) {
        m_shooterA.set(value);
        m_shooterB.set(-value);    // Might want to negate this before enable.
    }

    /**
     * Sets software position limits and sets an enable flag
     *
     * @param upper upper positional limit
     * @param lower lower positional limit
     * @param en enable/disable flag.
     */
    public void setSoftLimits(double upper, double lower, boolean en) {
        this.m_lowerLim = lower;
        this.m_upperLim = upper;
        this.m_limitsEnabled = en;
    }

    double manualInput = 0;

    /**
     * Sets manual input value for manual mode.
     *
     * @param input
     */
    public void setManual(double input) {
        this.manualInput = input;
    }

    /**
     * Contains state values for shooter system finite state machine.
     */
    public static class States {

        public final static int STOW = 0;
        public final static int SHOOT = 1;
        public final static int PASS = 2;
        public final static int TEST = -1;
        public final static int MANUAL = 99;
        
        public static String toString(int state) {
            if(state == STOW) {
                return "STOW";
            } else if(state == SHOOT) {
                return "SHOOT";
            } else if(state == PASS) {
                return "PASS";
            } else if(state == TEST) {
                return "TEST";
            } else if(state == MANUAL) {
                return "MANUAL";
            } else {
                return "ERROR";
            }
        }

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
        
        if(curr_state != prev_state) {
            System.out.println("[SHOOTER] State change from " + 
                                States.toString(prev_state) + " to " + 
                                States.toString(curr_state));
        }

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
            output = 0;
            if (this.getPosition() < Constants.Shooter.SHOT_POS) {
                if (this.getPosition() < Constants.Shooter.SHOT_START) {
                    output = Constants.Shooter.SHOT_INIT_SPEED;
                } else {
                    output = Constants.Shooter.SHOT_FINAL_SPEED;
                    if (this.getPosition() < Constants.Shooter.SHOT_POS - .05) {
                        this.setState(States.STOW);
                    }
                }
            } else {
                output = 0;
            }
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
            // Else if manual control, do this
            output = manualInput;
    /**        if (this.getPosition() > this.m_upperLim
                    && output > 0
                    && m_limitsEnabled) {
                output = 0;
            } else if (this.getPosition() < this.m_lowerLim
                    && output < 0
                    && m_limitsEnabled) {
                output = 0;
            }*/
        } else if(curr_state == States.TEST) {  //Auto Calibrate Mode
            Double newUpper = null;
            Double newLower = null;
            
            for(int i = 0; i < 4; i++) {
                System.out.println("[SHOOTER] Auto-Calibrate: Moving Down...");
                this.setOutput(-.1);
                Timer.delay(.5);
            }
            newLower = Double.valueOf(this.getPosition());
            System.out.println("[SHOOTER] Auto-Calibrate: New Lower Limit: " + newLower.doubleValue());
            for(int i = 0; i < 20; i++) {
                System.out.println("[SHOOTER] Auto-Calibrate: Moving Up...");
                this.setOutput(.15);
                Timer.delay(.5);
            }
            newUpper = Double.valueOf(this.getPosition());
            System.out.println("[SHOOTER] Auto-Calibrate: New Upper Limit: " + newUpper.doubleValue());
            this.setSoftLimits(newUpper.doubleValue(), newLower.doubleValue(), m_limitsEnabled);
            System.out.println("[SHOOTER] Auto-Calibrate Complete!");
            this.setState(States.MANUAL);
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
    private double pidControl(double p,
            double i,
            double d,
            double f,
            double s) {
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
