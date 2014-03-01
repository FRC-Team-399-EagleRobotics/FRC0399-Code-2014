/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team399.y2014.Utilities.Debouncer;
import org.team399.y2014.Utilities.EagleMath;
import org.team399.y2014.robot.Config.Constants;
import org.team399.y2014.robot.Systems.Robot;

/**
 * Command for driving linearly a specified distance with a max speed limit
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class DriveDistanceCommand extends Command {

    private double timeout = 0.0;
    private double distance = 0.0;
    private double speedLimit = 0.0;
    private boolean isDriveAtTarget = false;

    private Debouncer atGoal = new Debouncer(.250);

    public DriveDistanceCommand(double distance, double speed, double timeout) {
        this.timeout = timeout;
        this.distance = Robot.getInstance().drivetrain.inchesToTicks(distance);
        this.speedLimit = speed;
    }

    protected void initialize() {
        this.setTimeout(timeout);
        Robot.getInstance().drivetrain.getEncoderDisplacement(true);
    }

    double iErr = 0;
    double prevPos = 0;
    double displacement = 0;;
    
    protected void execute() {
        Robot.getInstance().shooter.run();
        
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

        throttle = EagleMath.cap(throttle, -this.speedLimit, this.speedLimit);
        turning = EagleMath.cap(turning, -this.speedLimit, this.speedLimit);

        Robot.getInstance().drivetrain.arcadeDrive(throttle, turning);

        isDriveAtTarget = atGoal.update(Math.abs(error) < Constants.DriveTrain.TICKS_TO_INCHES);
    }
    
    
    protected void interrupted() {
        
    }

    protected boolean isFinished() {
        return isDriveAtTarget || this.isTimedOut();
    }

    protected void end() {
        Robot.getInstance().drivetrain.arcadeDrive(0, 0);
    }
}
