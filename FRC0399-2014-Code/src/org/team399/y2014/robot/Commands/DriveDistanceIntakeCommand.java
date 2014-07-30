/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team399.y2014.Utilities.Debouncer;
import org.team399.y2014.Utilities.EagleMath;
import org.team399.y2014.robot.Config.Constants;
import org.team399.y2014.robot.Systems.Robot;

/**
 *
 * @author Ivan
 */
public class DriveDistanceIntakeCommand extends Command {
    private double speedDrive = 0.0;
    private double distance = 0.0;
    private double speedIntake = 0.0;
    private double turn = 0.0;
    private boolean state = Constants.Intake.RETRACTED;
    private double timeout = 0.0;
    private boolean isDriveAtTarget = false;
    private Debouncer atGoal = new Debouncer(.250);
    double iErr = 0;
    double prevPos = 0;
    double displacement = 0;
    double IntakeOutSpeed = -.75;
    
    public DriveDistanceIntakeCommand(double speedDrive,double distance, double speedIntake, double turn, boolean state, double timeout) {
        this.speedDrive = speedDrive;
        this.distance = Robot.getInstance().drivetrain.inchesToTicks(distance);
        this.speedIntake = speedIntake;
        this.turn = turn;
        this.state = state;
        this.timeout = timeout;
    }

    protected void execute() {
        Robot.getInstance().shooter.run();
        if (this.state == Constants.Intake.EXTENDED) {
            //If extended, run intake in to stage ball properly.
            Robot.getInstance().intake.setMotors(IntakeOutSpeed);
        prevPos = displacement;
        displacement = Robot.getInstance().drivetrain.getEncoderDisplacement(false);
        double encTurn = Robot.getInstance().drivetrain.getEncoderTurn(false);
        double error = distance - displacement;
        
        iErr += error;
        
        if(iErr > 1) {
            iErr = 1;
        } else if(iErr < -1) {
            iErr = -1;
        }

        double throttle = -(error * Constants.DriveTrain.DRIVE_P) + 
                (Constants.DriveTrain.DRIVE_I * iErr) 
                - (Constants.DriveTrain.DRIVE_D * (prevPos - displacement));
        double turning = encTurn * Constants.DriveTrain.STRAIGHT_P;
        System.out.println("[DRIVE] Disp : " + displacement);
        System.out.println("[DRIVE] Error: " + error);

        throttle = EagleMath.cap(throttle, -this.speedDrive, this.speedDrive);
        turning = EagleMath.cap(turning, -this.speedDrive, this.speedDrive);

        Robot.getInstance().drivetrain.arcadeDrive(throttle, turning);

        isDriveAtTarget = atGoal.update(Math.abs(error) < Constants.DriveTrain.TICKS_TO_INCHES);
        if(error > -1200){
            IntakeOutSpeed =- 0.05;
        }
    }
    }
    protected void interrupted() {
         

    }

    protected void end() {

        Robot.getInstance().intake.setMotors(this.speedIntake);
         Robot.getInstance().drivetrain.arcadeDrive(0, 0);
    }

    protected void initialize() {
        Robot.getInstance().intake.setActuators(this.state);
        this.setTimeout(timeout);
         Robot.getInstance().drivetrain.arcadeDrive(speedDrive, turn);
        setTimeout(timeout);
    }

    protected boolean isFinished() {
         return isDriveAtTarget || this.isTimedOut();
    }

}

