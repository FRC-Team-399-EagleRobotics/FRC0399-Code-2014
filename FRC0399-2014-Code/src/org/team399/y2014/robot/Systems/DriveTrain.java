
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Systems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team399.y2014.Utilities.EagleMath;
import org.team399.y2014.robot.Config.Constants;

/**
 * Contains functions related to input and output of the drivetrain subsystem.
 *
 * @author ivansalazar003@gmail.com (Ivan Salazar)
 */
public class DriveTrain {

    private Talon leftA = null;
    private Talon leftB = null;
    private Talon leftC = null;
    private Talon rightA = null;
    private Talon rightB = null;
    private Talon rightC = null;
    private Gyro yaw = null;
    public Encoder leftEnc = null;
    public Encoder rightEnc = null;
    private int fsmState = 0;

    /**
     * Constructor
     *
     * @param lA Left motor A PWM port
     * @param lB left motor B PWM port
     * @param rA right motor A PWM port.
     * @param rB right motor B PWM port
     */
    public DriveTrain(int left, int right) {
        leftA = new Talon(left);
        rightA = new Talon(right);

    }

    /**
     * Constructor
     *
     * @param lA Left motor A PWM port
     * @param lB left motor B PWM port
     * @param lC left motor C PWM port
     * @param rA right motor A PWM port
     * @param rB right motor B PWM port
     * @param rC right motor C PWM port
     */
    public DriveTrain(int lA, int lB, int lC, int rA, int rB, int rC) {
        leftA = new Talon(lA);
        leftB = new Talon(lB);
        leftC = new Talon(lC);

        rightA = new Talon(rA);
        rightB = new Talon(rB);
        rightC = new Talon(rC);
    }

    /**
     * Initialize sensors
     *
     * @param gyro Gyro analog port
     * @param leftEncA Encoder port - left A
     * @param leftEncB
     * @param rightEncA
     * @param rightEncB
     */
    public void setSensors(int gyro, int leftEncA, int leftEncB, int rightEncA, int rightEncB) //gets angle and constrains it between 0 and 359 then returns it
    {
        yaw = new Gyro(gyro);
        leftEnc = new Encoder(leftEncA, leftEncB);
        rightEnc = new Encoder(rightEncA, rightEncB);
        leftEnc.start();
        leftEnc.reset();
        rightEnc.start();
        rightEnc.reset();
        yaw.reset();
    }

    /**
     * Reinitializes sensors to default values.
     */
    public void resetSensors() {
        yaw.reset();
        leftEnc.reset();
        rightEnc.reset();
    }

    /**
     * Outputs power to the left and right sides of the drivetrain.
     *
     * @param leftPower
     * @param rightPower
     */
    public void tankDrive(double leftPower, double rightPower) {
        leftPower = -leftPower;
        rightPower = -rightPower;

        if (Math.abs(leftPower) < .1) {
            leftPower = 0;
        }
        if (Math.abs(rightPower) < .1) {
            rightPower = 0;
        }
        leftA.set(leftPower);
        if (leftB != null) {
            //      leftB.set(leftPower);
        }

        if (leftC != null) {
            //        leftC.set(leftPower);
        }

        rightA.set(-rightPower);
        if (rightB != null) {
            //          rightB.set(-rightPower);
        }
        if (rightC != null) {
//            rightC.set(rightPower);
        }
    }

    /**
     * Sets tank drive inputs for FSM use only!
     *
     * @param tank_left
     * @param tank_right
     */
    public void setTankDrive(double tank_left, double tank_right) {
        this.tank_left = tank_left;
        this.tank_right = tank_right;
    }

    /**
     * Arcade drive function. takes a throttle and turning rate input and
     * converts it into left/right tank drive controls.
     *
     * @param throttle
     * @param turning
     */
    public void arcadeDrive(double throttle, double turning) {
        tankDrive(throttle + turning, throttle - turning);
    }

    /**
     * Conversion from inches to encoder ticks.
     *
     * @param inches
     * @return a distance in encoder ticks.
     */
    public double inchesToTicks(double inches) {
        return inches * Constants.DriveTrain.TICKS_TO_INCHES;
    }

    /**
     * Returns the current value of the gyro sensor, constrained from -180 to
     * 180.
     *
     * @return
     */
    public double getHeading() {
        double heading = yaw.getAngle();
        heading = EagleMath.constrainAngle(heading, -180, 180);
        heading = EagleMath.truncate(heading, 4);
        return heading;
    }

    /**
     * Returns the difference between the encoder values on the left and right
     * drivetrain sides
     *
     * @return
     */
    public double getEncoderTurn() {
        return getEncoderTurn(false);
    }

    /**
     * Returns the difference between the encoder values on the left and right
     * drivetrain sides.
     *
     * @param clear set to true to reset the encoder value.
     * @return
     */
    public double getEncoderTurn(boolean clear) {
        double left =  -1*leftEnc.get(); // practivce bot, this value needs to be negated
        double right = rightEnc.get();
        leftE = left;
        rightE = right;

        if (clear) {
            leftEnc.reset();
            rightEnc.reset();
        }

        return (left - right);
    }

    /**
     * Get the encoder displacement from the last reset
     *
     * @return
     */
    public double getEncoderDisplacement() {
        return getEncoderDisplacement(false);
    }
    public double leftE = 0;
    public double rightE = 0;

    /**
     * Get the encoder displacement from the last reset
     *
     * @param clear set to true to reset the encoders.
     * @return
     */
    public double getEncoderDisplacement(boolean clear) {
        double left =  leftEnc.get();
        double right = rightEnc.get();
        leftE = left;
        rightE = right;

        if (clear) {
            leftEnc.reset();
            rightEnc.reset();
        }

        return (right);
    }

    /**
     * States for drivetrain finite state machine.
     */
    public static class States {

        public final static int TANK_DRIVE = 0;
        public final static int ARCADE_DRIVE = 1;
        public final static int PID_TURN = 2;
        public final static int PID_DRIVE = 3;
        public final static int PID_BRAKE = 4;
    }
    private int curr_state = 0, prev_state = 0;

    public void setState(int newState) {
        prev_state = curr_state;
        curr_state = newState;
    }

    public int getState() {
        return curr_state;
    }
    //Drive FSM
    //0 = tank drive
    //1 = arcade drive
    //2 = PID Turn
    //3 = PID distance
    //4 = Reset
    //FSM state values:
    private double tank_left = 0;
    private double tank_right = 0;
    private double arcade_throttle = 0;
    private double arcade_turning = 0;
    private double dist_distance = 0;
    private double turn_angle = 0;
    private double goal = 0.0;
    private int prevState = 0;

    public void run() {
        if (curr_state != prevState) {
            //state change
            if (curr_state == States.PID_BRAKE) {
                this.getEncoderDisplacement(true);
                System.out.println("encoder reset");
            }
        }

        double outL = 0.0, outR = 0.0;

        System.out.println("[DRIVE] State: " + curr_state);

        if (curr_state == States.TANK_DRIVE) {
            outL = tank_left;
            outR = tank_right;
        } else if (curr_state == States.ARCADE_DRIVE) {
        } else if (curr_state == 2) {
        } else if (curr_state == 4) {

            outL = this.getEncoderDisplacement(false) * Constants.DriveTrain.DRIVE_P;
            outR = outL;
            System.out.println("output" + outL);
        }

        tankDrive(outL, outR);
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
    public static String toString(int state) {
        return "error";
    }

    private double pidControl(double p,
            double i,
            double d,
            double f,
            double s) {
        prevError = error;
        error = this.getEncoderDisplacement(false);

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

        return -output;
    }
}
