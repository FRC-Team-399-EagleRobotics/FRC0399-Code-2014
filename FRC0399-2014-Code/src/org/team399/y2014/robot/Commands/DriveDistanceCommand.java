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
public abstract class DriveDistanceCommand extends Command {

    private double timeout = 0.0;
    private double distance = 0.0;
    private double speedLimit = 0.0;
    private boolean isDriveAtTarget = false;

    private Debouncer atGoal = new Debouncer(.250);

    public DriveDistanceCommand(double distance, double speed, double timeout) {
        this.timeout = timeout;
        this.distance = distance;
        this.speedLimit = speed;
    }

    protected void initialize() {
        this.setTimeout(timeout);
        Robot.getInstance().drivetrain.getEncoderDisplacement(true);
    }

    protected void execute() {
        double displacement
                = Robot.getInstance().drivetrain.getEncoderDisplacement(false);
        double encTurn = Robot.getInstance().drivetrain.getEncoderTurn(false);
        double error = distance - displacement;

        double throttle = error * Constants.DriveTrain.DRIVE_P;
        double turning = encTurn * Constants.DriveTrain.STRAIGHT_P;

        throttle = EagleMath.cap(throttle, -this.speedLimit, this.speedLimit);
        turning = EagleMath.cap(turning, -this.speedLimit, this.speedLimit);

        Robot.getInstance().drivetrain.arcadeDrive(0, 0);

        isDriveAtTarget = atGoal.update(Math.abs(error) < Constants.DriveTrain.TICKS_TO_INCHES);
    }

    protected boolean isFinished() {
        return isDriveAtTarget || this.isTimedOut();
    }

    protected void end() {
        Robot.getInstance().drivetrain.arcadeDrive(0, 0);
    }
}
