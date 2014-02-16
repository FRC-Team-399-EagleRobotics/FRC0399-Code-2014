/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team399.y2014.robot.Systems.Robot;

/**
 * Command for simple timed arcade drive movement.
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class ArcadeDriveCommand extends Command {

    private double speed = 0.0;
    private double turn = 0.0;
    private double timeout = 0.0;

    public ArcadeDriveCommand(double speed, double turn, double timeout) {
        this.speed = speed;
        this.turn = turn;
        this.timeout = timeout;
    }

    protected void initialize() {
        Robot.getInstance().drivetrain.arcadeDrive(speed, turn);
        setTimeout(timeout);
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void execute() {

    }

    protected void end() {
        Robot.getInstance().drivetrain.arcadeDrive(0, 0);
    }

    protected void interrupted() {
        Robot.getInstance().drivetrain.arcadeDrive(0, 0);
    }
}
