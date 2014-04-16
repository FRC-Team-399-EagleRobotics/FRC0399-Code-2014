/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team399.y2014.Utilities.CheesyVisionServer;
import org.team399.y2014.robot.Main;
import org.team399.y2014.robot.Systems.Robot;
import org.team399.y2014.robot.Systems.Vision.Target;

/**
 * Command to wait for the vision algorithm to process an image.
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class CheesyVisionTestCommand extends Command {
    
    public final int listenPort = 1180;
    private double timeout = 0.0;
    private boolean found = false;

    int delay = 50;

    public CheesyVisionTestCommand() {
        //this.timeout = timeout;
        
    }

    protected void initialize() {
        //this.setTimeout(timeout);
        //Main.server.setPort(listenPort);
        //Main.server.start();
        Main.server.reset();
        Main.server.startSamplingCounts();
        
    }
    

    protected void execute() {

        // CheesyVision code indicates hot goal state using counters.
        // It's best to determine hot goal state from the robot code using
        // a comparison between the left and right counters to prevent
        // accidental misfires. Edit the field 'delay' above to tune the timer.

        System.out.println("Vision: ");
        System.out.println("Current left: " + Main.server.getLeftStatus() + ", current right: " + Main.server.getRightStatus());
        System.out.println("Left count: " + Main.server.getLeftCount() + ", right count: " + Main.server.getRightCount() + ", total: " + Main.server.getTotalCount() + "\n");
        

        // target is found if operator
        // indicates hot goal initially
//        found = (left - right > delay)
//                || (right - left > delay);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        Main.server.stop();

    }

    protected void interrupted() {
        Main.server.stop();
    }
}
