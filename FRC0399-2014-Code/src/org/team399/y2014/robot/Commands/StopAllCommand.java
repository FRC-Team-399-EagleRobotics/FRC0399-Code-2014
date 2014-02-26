/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team399.y2014.robot.Systems.Robot;
import org.team399.y2014.robot.Systems.Shooter.States;

/**
 * Command for a simple do nothing and wait
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class StopAllCommand extends Command {

    public StopAllCommand() {
    }

    protected void initialize() {
        Robot.getInstance().shooter.setState(States.MANUAL);
        Robot.getInstance().shooter.setManual(0);
        Robot.getInstance().drivetrain.tankDrive(0, 0);
        Robot.getInstance().intake.setMotors(0);
    }
    
    protected void execute() {
        Robot.getInstance().shooter.run();
    }
    
    protected void interrupted() {
        
    }
    
    protected void end() {
        
    }

    protected boolean isFinished() {
        return this.isTimedOut();
    }
}
