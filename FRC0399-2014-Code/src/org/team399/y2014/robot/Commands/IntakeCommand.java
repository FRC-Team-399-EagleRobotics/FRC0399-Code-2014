/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team399.y2014.robot.Config.Constants;
import org.team399.y2014.robot.Systems.Robot;

/**
 * Command to drive the intake motors and actuate the intake position
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class IntakeCommand extends Command {

    private double speed = 0.0;
    private boolean state = Constants.Intake.RETRACTED;
    private double timeout = 0.0;

    public IntakeCommand(double speed, boolean state, double timeout) {
        this.speed = speed;
        this.state = state;
        this.timeout = timeout;
    }
    
    protected void execute() {
        
    }
    
    protected void interrupted() {
        
    }
    
    protected void end() {
        
    }

    protected void initialize() {
        Robot.getInstance().intake.setActuators(this.state);
        Robot.getInstance().intake.setMotors(this.speed);
        this.setTimeout(timeout);
    }

    protected boolean isFinished() {
        return this.isTimedOut();
    }

}
