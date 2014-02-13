/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team399.y2014.robot.Systems.Robot;
import org.team399.y2014.robot.Systems.Shooter;
import org.team399.y2014.robot.Systems.Shooter.States;

/**
 * Command to shoot a ball
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public abstract class ShootCommand extends Command {

    private double timeout = 0.0;

    public ShootCommand(double timeout) {
        this.timeout = timeout;
    }

    protected void initialize() {
        this.setTimeout(timeout);
        Robot.getInstance().shooter.setState(Shooter.States.SHOOT);
    }

    protected void execute() {

    }

    protected void end() {
        Robot.getInstance().shooter.setState(Shooter.States.STAGE);
    }

    protected boolean isFinished() {
        return Robot.getInstance().shooter.getShootDone();
    }
}
