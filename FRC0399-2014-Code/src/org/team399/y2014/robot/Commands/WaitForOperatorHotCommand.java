/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team399.y2014.Utilities.CheesyVisionServer;

/**
 * Command to wait for the operator interface to get signal from the operator
 * indicating a which goal is hot.
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class WaitForOperatorHotCommand extends Command {

    
    public final int listenPort = 1180;
    private double timeout = 0.0;
    private boolean found = false;

    int delay = 50;

    public WaitForOperatorHotCommand(double timeout) {
        this.timeout = timeout;
    }

    protected void initialize() {
        this.setTimeout(timeout);

    }

    protected void execute() {

        // CheesyVision code indicates hot goal state using counters.
        // It's best to determine hot goal state from the robot code using
        // a comparison between the left and right counters to prevent
        // accidental misfires. Edit the field 'delay' above to tune the timer.
        int left = CheesyVisionServer.getInstance().getLeftCount();
        int right = CheesyVisionServer.getInstance().getRightCount();

        System.out.println("Vision: ");
        System.out.println("L: " + left);
        System.out.println("R: " + right);

        // target is found if operator
        // indicates hot goal initially
        found = (left - right > delay)
                || (right - left > delay);
    }

    protected boolean isFinished() {
        return found || this.isTimedOut();
    }

    protected void end() {

    }

    protected void interrupted() {

    }
}
