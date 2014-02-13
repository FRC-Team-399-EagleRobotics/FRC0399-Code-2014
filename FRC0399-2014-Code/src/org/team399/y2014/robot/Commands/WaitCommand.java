/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command for a simple do nothing and wait
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public abstract class WaitCommand extends Command {

    private double timeout = 0.0;

    public WaitCommand(double timeout) {
        this.timeout = timeout;
    }

    protected void initialize() {
        this.setTimeout(timeout);
    }

    protected boolean isFinished() {
        return this.isTimedOut();
    }
}
