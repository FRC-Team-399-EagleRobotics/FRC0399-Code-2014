
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
 *
 * @author Ivan
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

    public void resetSensors() {
        yaw.reset();
        leftEnc.reset();
        rightEnc.reset();
    }

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

    public void setTankDrive(double tank_left, double tank_right) {
        this.tank_left = tank_left;
        this.tank_right = tank_right;
    }

    public void arcadeDrive(double throttle, double turning) {
        tankDrive(throttle + turning, throttle - turning);
    }

    public double inchesToTicks(double inches) {
        return inches * Constants.DriveTrain.TICKS_TO_INCHES;
    }

    public double getHeading() {
        double heading = yaw.getAngle();
        heading = EagleMath.constrainAngle(heading, -180, 180);
        heading = EagleMath.truncate(heading, 4);
        return heading;
    }

    public double getEncoderTurn() {
        return getEncoderTurn(true);
    }

    public double getEncoderTurn(boolean clear) {
        double left = leftEnc.get();
        double right = rightEnc.get();
        leftE = left;
        rightE = right;

        if (clear) {
            leftEnc.reset();
            rightEnc.reset();
        }

        return (left - right);
    }

    public double getEncoderDisplacement() {
        return getEncoderDisplacement(true);
    }
    public double leftE = 0;
    public double rightE = 0;

    public double getEncoderDisplacement(boolean clear) {
        double left = -leftEnc.get();
        double right = rightEnc.get();
        leftE = left;
        rightE = right;

        if (clear) {
            leftEnc.reset();
            rightEnc.reset();
        }

        return (left + right) / 2.0;
    }

    public double twoStickToTurning(double left, double right) {
        return (left - right) / 2;
    }

    public double twoStickToThrottle(double left, double right) {
        return (left + right) / 2;
    }
    private double old_wheel = 0.0;
    private double neg_inertia_accumulator = 0.0;
    private static final double CD_SENS_HIGH = 0.875;
    private static final double CD_SENS_LOW = 1.111;
    private static final double CD_WHEEL_NONLIN_HIGH = 1.0;
    private static final double CD_WHEEL_NONLIN_LOW = 0.8;
    private static final double CD_NEG_INERTIA = 3.0;

    public void cheesyDrive(double leftP, double rightP) {
        double wheel = twoStickToTurning(leftP, rightP);
        double throttle = twoStickToTurning(leftP, rightP);

        double left_pwm, right_pwm, overPower;
        double sensitivity = 1.2;
        double angular_power;
        double linear_power;
        double wheelNonLinearity;
        boolean quickTurn = Math.abs(throttle) < .05;//Math.abs(wheel) > .375 &&

        double neg_inertia = wheel - old_wheel;
        old_wheel = wheel;

        wheelNonLinearity = CD_WHEEL_NONLIN_HIGH;        //Used to be .9 higher is less sensitive
        // Apply a sin function that's scaled to make it feel bette
//            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / Math.sin(Math.PI / 2.0 * wheelNonLinearity);

        double neg_inertia_scalar;
        if (wheel * neg_inertia > 0) {
            neg_inertia_scalar = CD_NEG_INERTIA * 1.66;
        } else {
            if (Math.abs(wheel) > 0.65) {
                neg_inertia_scalar = CD_NEG_INERTIA * 3.33;
            } else {
                neg_inertia_scalar = CD_NEG_INERTIA;
            }
        }
        sensitivity = CD_SENS_HIGH; //lower is less sensitive

        if (Math.abs(throttle) > 0.1) {
            sensitivity = .9 - (.9 - sensitivity) / Math.abs(throttle);
        }
        //neg_inertia_scalar *= .4;
        double neg_inertia_power = neg_inertia * neg_inertia_scalar;
        if (Math.abs(throttle) >= 0.05 || quickTurn) {
            neg_inertia_accumulator += neg_inertia_power;
        }
        wheel = wheel + neg_inertia_accumulator;
        if (neg_inertia_accumulator > 1) {
            neg_inertia_accumulator -= 1;
        } else if (neg_inertia_accumulator < -1) {
            neg_inertia_accumulator += 1;
        } else {
            neg_inertia_accumulator = 0;
        }

        linear_power = throttle;

        if ((!EagleMath.isInBand(throttle, -0.2, 0.2) || !(EagleMath.isInBand(wheel, -0.65, 0.65))) && quickTurn) {
            overPower = 1.0;
            sensitivity = 1.0;
            sensitivity = 1.0;
            angular_power = wheel;
        } else {
            overPower = 0.0;
            angular_power = Math.abs(throttle) * wheel * sensitivity;
        }

        if (quickTurn) {
            angular_power = EagleMath.signedSquare(angular_power, 1);   //make turning less sensitive under quickturn
            if (Math.abs(angular_power) >= .745) {
                //    angular_power = 1.0*EagleMath.signum(angular_power);
            }
        }

        right_pwm = left_pwm = linear_power;
        left_pwm += angular_power;
        right_pwm -= angular_power;

        if (left_pwm > 1.0) {
            right_pwm -= overPower * (left_pwm - 1.0);
            left_pwm = 1.0;
        } else if (right_pwm > 1.0) {
            left_pwm -= overPower * (right_pwm - 1.0);
            right_pwm = 1.0;
        } else if (left_pwm < -1.0) {
            right_pwm += overPower * (-1.0 - left_pwm);
            left_pwm = -1.0;
        } else if (right_pwm < -1.0) {
            left_pwm += overPower * (-1.0 - right_pwm);
            right_pwm = -1.0;
        }
        tankDrive((left_pwm), (right_pwm));
    }

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
                System.out.println( "encoder reset");
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
        error = this.getEncoderDisplacement(false) ;

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
