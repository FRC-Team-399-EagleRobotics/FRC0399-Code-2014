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

    private Talon leftA = null;// = new Jaguar(1);
    private Talon leftB = null;// = new Jaguar(2);
    private Talon leftC = null;
    private Talon rightA = null;// = new Jaguar(3);
    private Talon rightB = null;// = new Jaguar(4);
    private Talon rightC = null;
    private Gyro yaw = null;
    private Encoder leftEnc = null;
    private Encoder rightEnc = null;

    private int fsmState = 0;
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

    private double turnP = Constants.TURN_P;
    private double turnI = Constants.TURN_I;
    private double turnD = Constants.TURN_D;
    private double turnErr;
    private double intErr = 0;
    private double prevHeading = 0;
    private boolean isTurnAtTarget = false;
    double driveP = Constants.DRIVE_P;
    boolean isDriveAtTarget = false;

    /**
     * Constructor
     *
     * @param lA Left motor A PWM port
     * @param lB left motor B PWM port
     * @param rA right motor A PWM port.
     * @param rB right motor B PWM port
     */
    public DriveTrain(int lA, int lB, int rA, int rB) {
        leftA = new Talon(lA);
        leftB = new Talon(lB);
        leftC = null;
        rightA = new Talon(rA);
        rightB = new Talon(rB);
        rightC = null;

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
    }

    public void resetSensors()//DONT KNOW FUNCTION???
    {
        yaw.reset();
        leftEnc.reset();
        rightEnc.reset();
    }

    public void tankDrive(double leftPower, double rightPower) //DONT KNOW FUNCTION???
    {
        leftA.set(leftPower);
        leftB.set(leftPower);

        if (leftC != null) {
            leftC.set(leftPower);
        }

        rightA.set(-rightPower);
        rightB.set(-rightPower);

        if (rightC != null) {
            rightC.set(rightPower);
        }
    }

    public void arcadeDrive(double throttle, double turning) {
        tankDrive(throttle + turning, throttle - turning);
    }

    public void driveFsm() {
        if (fsmState == 0) {
            tankDrive(tank_left, tank_right);
        } else if (fsmState == 1) {
            arcadeDrive(arcade_throttle, arcade_turning);
        } else if (fsmState == 2) {
            PIDTurn(turn_angle);
        } else if (fsmState == 3) {
            PIDDrive(dist_distance);
        } else if (fsmState == 4) {

        }
    }

    public double PIDTurn(double setpoint) {
        if (Math.abs(turnErr) < 5) {
            turnP = SmartDashboard.getNumber("turnPLow", turnP);
            turnI = SmartDashboard.getNumber("turnIlow", turnI);
            turnD = SmartDashboard.getNumber("turnDlow", turnD);
            //  System.out.println("low");

        } else if (Math.abs(turnErr) > 5) {
            turnP = SmartDashboard.getNumber("turnPhigh", turnP);
            turnI = SmartDashboard.getNumber("turnIhigh", turnI);
            turnD = SmartDashboard.getNumber("turnDhigh", turnD);
            // System.out.println("high");
        }

        double heading = getHeading();
        turnErr = setpoint - heading;
        intErr += turnI * turnErr / 100.0;

        if (intErr > 1.0) {
            intErr = 1.0;

        } else if (intErr < -1.0) {
            intErr = -1.0;
        }

        double dOut = turnD * (heading - prevHeading) / 100.0;
        prevHeading = heading;

        //setpoint = setpoint + NavigationUtils.getInstance().turningDirection(setpoint, NavigationUtils.getInstance().getHeading());
        //System.out.println("ERR: " + turnErr);
        isTurnAtTarget = Math.abs(turnErr) < 1;

        return turnP * turnErr / 100.0 + intErr + dOut;  // what are you doing with this value?
    }

    public double PIDDrive(double setpoint) {
        double distance = getEncoderDisplacement(false);
        double error = setpoint - distance;

        isDriveAtTarget = Math.abs(error) < 16.56;

        return driveP * error;
    }

    public double inchesToTicks(double inches)//gets angle and constrains it between 0 and 359 then returns it
    {
        return inches * Constants.TICKS_TO_INCHES;
    }

    public double getHeading() //gets angle and constrains it between 0 and 359 then returns it
    {
        double heading = yaw.getAngle();
        heading = EagleMath.constrainAngle(heading, -180, 180);
        return heading;
    }

    public double getEncoderTurn()//DONT KNOW FUNCTION???
    {
        return getEncoderTurn(true);//DONT KNOW??
    }

    public double getEncoderTurn(boolean clear) //DONT KNOW FUNCTION???
    {
        double left = leftEnc.getDistance();
        double right = rightEnc.getDistance();

        if (clear) {
            leftEnc.reset();
            rightEnc.reset();
        }

        return (left - right) / Constants.WIDTH;
    }

    public double getEncoderDisplacement() {
        return getEncoderDisplacement(true);//returns encoderdisplacement
    }

    public double getEncoderDisplacement(boolean clear) {
        double left = leftEnc.getDistance();
        double right = rightEnc.getDistance();

        if (clear) {
            leftEnc.reset();
            rightEnc.reset();
        }

        return (left + right) / 2.0;
    }

    public void updateSmartboard() {

        turnI = SmartDashboard.getNumber("turn_I", turnI);
        turnD = SmartDashboard.getNumber("turn_D", turnD);

        driveP = SmartDashboard.getNumber("drive_P", driveP);
        turnP = SmartDashboard.getNumber("turn_P", turnP);
        turnI = SmartDashboard.getNumber("turn_I", turnI);
        turnD = SmartDashboard.getNumber("turn_D", turnD);
        //turnSet = SmartDashboard.getNumber("turnSet", turnSet);

        SmartDashboard.putNumber("turnErr", turnErr);

        SmartDashboard.putNumber("turn_P", turnP);
        SmartDashboard.putNumber("turn_I", turnI);
        turnP = SmartDashboard.getNumber("turnP", turnP);
        turnI = SmartDashboard.getNumber("turnI", turnI);

    }
}
