/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team399.y2014.Utilities.Debouncer;
import org.team399.y2014.Utilities.EagleMath;
import org.team399.y2014.Utilities.ThrottledPrinter;
import org.team399.y2014.robot.Config.Constants;

/**
 * Shooter class. Base functions for shooter mechanism
 *
 * @author (jeremy.germita@gmail.com) Jeremy Germita
 * @author (ivansalazar003@gmail.com) Ivan Salazar
 */
public class Shooter {

    private Talon m_shooterA = null;    // Motor Controllers
    private Talon m_shooterB = null;
    private AnalogChannel m_pot = null; // Position sensor
    private DigitalInput m_switch = null; //auto-zero switch
    private double goal = 0;
    private double m_upperLim = 0.0;
    private double m_lowerLim = 5.0;
    private boolean m_limitsEnabled = false;
    private ThrottledPrinter fsmStatus = new ThrottledPrinter(0.5);

    /**
     * Constructor
     *
     * @param a motor controller A port
     * @param b motor controller B port
     * @param p potentiometer/position sensor port.
     * @param z zero switch port
     */
    public Shooter(int a, int b, int p, int z) {
        m_shooterA = new Talon(a);
        m_shooterB = new Talon(b);
        m_pot = new AnalogChannel(p);
        m_switch = new DigitalInput(z);
        this.setSoftLimits(Constants.Shooter.LOWER_LIMIT,
                Constants.Shooter.UPPER_LIMIT + Constants.Shooter.LOWER_LIMIT,
                false);
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
    private Debouncer zeroSwitchDebouncer = new Debouncer(.125);

    /**
     * Gets debounced state of bottom limit zero switch.
     *
     * @return 125 msec debounced limit switch input
     */
    public boolean getZeroSwitch() {
        boolean answer = !m_switch.get();
        answer = zeroSwitchDebouncer.update(answer);
        //System.out.println(answer);
        return answer;
    }

    /**
     * Gets position sensor value.
     *
     * @return Potentiometer value
     */
    public double getOffsetFromBottom() {
        double answer = this.getPosition() - Constants.Shooter.LOWER_LIMIT;
        return answer;
    }

    /**
     * Sets motor controller outputs.
     *
     * @param value
     */
    public void setOutput(double value) {
        if (Math.abs(value) > Constants.Shooter.SPEED_LIMIT) {
            value = Constants.Shooter.SPEED_LIMIT * EagleMath.signum(value);
        }

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
        Constants.Shooter.UPPER_LIMIT = this.m_upperLim;
        Constants.Shooter.LOWER_LIMIT = this.m_lowerLim;
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
    
    double goalOffset = 0.0;
    
    public void setGoalOffset(double goalOffset) {
        this.goalOffset = goalOffset;
    }
    
    

    /**
     * Contains state values for shooter system finite state machine.
     */
    public static class States {

        /**
         * Stow state is downward position inside the frame.
         */
        public final static int STOW = 0;
        /**
         * Shoot state is a 18ft shot distance
         */
        public final static int SHOOT = 1;
        /**
         * Stage state is position for staging ball pre-shot and intake
         */
        public final static int STAGE = 2;
        /**
         * Test state is auto-calibrate mode
         */
        public final static int TEST = -1;
        /**
         * Manual state is joystick input open loop control
         */
        public final static int MANUAL = 99;
        /**
         * Truss state is for truss toss and catch
         */
        public final static int TRUSS = 3;
        /**
         * Hold state is for intake stowed ball holding
         */
        public final static int HOLD = 4;
        public final static int SHORT_SHOT = 5;
        public final static int SHORT_STAGE = 6;
        public final static int LIVE_CAL = -2;

        public static String toString(int state) {
            if (state == STOW) {
                return "STOW";
            } else if (state == SHOOT) {
                return "SHOOT";
            } else if (state == STAGE) {
                return "STAGE";
            } else if (state == TEST) {
                return "TEST";
            } else if (state == MANUAL) {
                return "MANUAL";
            } else if (state == TRUSS) {
                return "TRUSS";
            } else if (state == SHORT_SHOT) {
                return "SHORT_SHOT";
            } else if (state == SHORT_STAGE) {
                return "SHORT_STAGE";
            } else if (state == HOLD) {
                return "HOLD";
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
    public long timeStateChange = 0;

    /**
     * Runs the shooter finite state machine with control loops
     */
    public void run() {
        double output = 0;

        //FSM status printouts for debugging
        this.fsmStatus.println("[SHOOTER] State is: "
                + States.toString(curr_state) + " (" + curr_state + ")");
        if (curr_state != prev_state) {
            System.out.println("[SHOOTER] State change from "
                    + States.toString(prev_state) + " to "
                    + States.toString(curr_state));
            timeStateChange = System.currentTimeMillis();
        }

        boolean zero = this.getZeroSwitch();

        //FSM logic
        if (curr_state == States.STOW) {
            // If stow, do this
            goal = Constants.Shooter.STOW_POS;
            output = pidControl(
                    Constants.Shooter.STOW_P,
                    Constants.Shooter.STOW_I,
                    Constants.Shooter.STOW_D,
                    Constants.Shooter.STOW_F,
                    Constants.Shooter.STOW_S);
        } else if (curr_state == States.SHORT_SHOT) {
            // Else if shoot, do this
            output = 0;
            double s = 0.0;
            s = Constants.Shooter.SHORT_S;
            goal = Constants.Shooter.SHORT_POS;
            output = pidControl(
                    Constants.Shooter.SHOT_P,
                    Constants.Shooter.SHOT_I,
                    Constants.Shooter.SHOT_D,
                    Constants.Shooter.SHOT_F,
                    s);
            System.out.println("Shot! Output: " + output);
        } else if (curr_state == States.SHOOT) {
            // Else if shoot, do this
            output = 0;
            double s = 0.0;
            s = Constants.Shooter.SHOT_FINAL_SPEED;
            goal = Constants.Shooter.SHOT_POS + goalOffset;
            output = pidControl(
                    Constants.Shooter.SHOT_P,
                    Constants.Shooter.SHOT_I,
                    Constants.Shooter.SHOT_D,
                    Constants.Shooter.SHOT_F,
                    s);
        } else if (curr_state == States.STAGE) {
            // Pass do this
            goal = Constants.Shooter.STAGE_POS;
            output = pidControl(
                    Constants.Shooter.STAGE_P,
                    Constants.Shooter.STAGE_I,
                    Constants.Shooter.STAGE_D,
                    Constants.Shooter.STAGE_F,
                    Constants.Shooter.STAGE_S);
        } else if (curr_state == States.SHORT_STAGE) {
            // Pass do this
            goal = Constants.Shooter.SHORT_STAGE_POS;
            output = pidControl(
                    Constants.Shooter.STAGE_P,
                    Constants.Shooter.STAGE_I,
                    Constants.Shooter.STAGE_D,
                    Constants.Shooter.STAGE_F,
                    Constants.Shooter.STAGE_S);
        } else if (curr_state == States.TRUSS) {
            // Pass do this
            goal = Constants.Shooter.TRUSS_POS;
            output = pidControl(
                    Constants.Shooter.TRUSS_P,
                    Constants.Shooter.TRUSS_I,
                    Constants.Shooter.TRUSS_D,
                    Constants.Shooter.TRUSS_F,
                    Constants.Shooter.TRUSS_S);
        } else if (curr_state == States.HOLD) {
            // Pass do this
            goal = Constants.Shooter.HOLD_POS + goalOffset;
            output = pidControl(
                    Constants.Shooter.HOLD_P,
                    Constants.Shooter.HOLD_I,
                    Constants.Shooter.HOLD_D,
                    Constants.Shooter.HOLD_F,
                    Constants.Shooter.HOLD_S);

        } else if (curr_state == States.MANUAL) {
            // Else if manual control, do this
            output = manualInput;
            /*
             * if (this.getPosition() > this.m_upperLim && output > 0 &&
             * m_limitsEnabled) { output = 0; } else if (this.getPosition() <
             * this.m_lowerLim && output < 0 && m_limitsEnabled) { output = 0; }
             */
        } else if (curr_state == States.LIVE_CAL) {
            // Teleop phase autocal
            
            if(!zero) {
                output = .4;
            } else {
                output = 0;
            }
            
        } else if (curr_state == States.TEST) {  //Auto Calibrate Mode
            SmartDashboard.putBoolean("AUTOCALIBRATE", false);
            Double newUpper = null;
            Double newLower = null;

            for (int i = 0; i < 4; i++) {
                if (this.getZeroSwitch()) {
                    System.out.println("[SHOOTER] Auto-Calibrate: Limit Hit!");
                    break;
                }
                System.out.println("[SHOOTER] Auto-Calibrate: Moving Down...");
                this.setOutput(.15);
                Timer.delay(.5);
            }
            newLower = Double.valueOf(this.getPosition());
            System.out.println("[SHOOTER] Auto-Calibrate: New Lower Limit: " + newLower.doubleValue());
            newUpper = Double.valueOf(newLower.doubleValue() + Constants.Shooter.UPPER_LIMIT);
            System.out.println("[SHOOTER] Auto-Calibrate: New Upper Limit: " + newUpper.doubleValue());
            this.setSoftLimits(newUpper.doubleValue(), newLower.doubleValue(), m_limitsEnabled);
            System.out.println("[SHOOTER] Auto-Calibrate Complete! New Limits: L: " + newLower.doubleValue() + " U: " + newUpper.doubleValue());
            this.setState(States.MANUAL);
            this.setManual(0);
            SmartDashboard.putBoolean("AUTOCALIBRATE", true);
        } else {
            System.out.println("[SHOOTER] Invalid State!!");
        }

        SmartDashboard.putNumber("ShooterOut", output);



        SmartDashboard.putBoolean("ZERO", zero);
        if (zero) {


            if (output > 0) {
                output = 0;
            }
            
            this.setSoftLimits(this.getPosition(), this.m_upperLim, true);
            System.out.println("[SHOOTER] Zero!");
        }
        if (output > Constants.Shooter.DOWN_SPEED) {
            output = Constants.Shooter.DOWN_SPEED;
        }


        this.setOutput(output);
    }

    /**
     * Gets the state of a timer indicating whether or not a shot has been
     * assumed as complete
     *
     * @return true if the fsm has been in the shooter state for 800ms
     */
    public boolean getShootDone() {
        return (curr_state == States.SHOOT || curr_state == States.SHORT_SHOT)
                && (System.currentTimeMillis() - timeStateChange > 800);
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
        error = this.getPosition() - (goal + this.m_lowerLim);

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
