/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team399.y2014.robot.Config.Constants;
import org.team399.y2014.robot.Systems.Robot;

/**
 *
 * @author Ivan
 */
public class IntakeTimeDriveCommand  extends Command{
     private double speedDrive = 0.0;
     private double speedIntake = 0.0;
     private double turn = 0.0;
    private boolean state = Constants.Intake.RETRACTED;
    private double timeout = 0.0;

    public IntakeTimeDriveCommand(double speedDrive,double speedIntake, double turn, boolean state, double timeout) {
        this.speedDrive = speedDrive;
        this.speedIntake = speedIntake;
        this.turn = turn;
        this.state = state;
        this.timeout = timeout;
    }

    protected void execute() {
        Robot.getInstance().shooter.run();
        if (this.state == Constants.Intake.EXTENDED) {
            //If extended, run intake in to stage ball properly.
            Robot.getInstance().intake.setMotors(-.55);
            
            
        }
    }

    protected void interrupted() {
         Robot.getInstance().drivetrain.arcadeDrive(0, 0);

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
        return this.isTimedOut();
    }

}
    

