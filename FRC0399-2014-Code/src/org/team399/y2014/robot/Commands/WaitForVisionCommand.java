/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team399.y2014.robot.Systems.Robot;
import org.team399.y2014.robot.Systems.Vision.Target;

/**
 * Command to wait for the vision algorithm to process an image.
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public abstract class WaitForVisionCommand extends Command {

    private double timeout = 0.0;
    private boolean found = false;

    public WaitForVisionCommand(double timeout) {
        this.timeout = timeout;
    }

    protected void intialize() {
        this.setTimeout(timeout);
    }

    protected void run() {
        Target targ = Robot.getInstance().camera.getTarget();
        found = (targ.x != 0 && targ.y != 0);   //might need to rethink found criteria

    }

    protected boolean isFinished() {
        return this.isTimedOut() || found;
    }
}
