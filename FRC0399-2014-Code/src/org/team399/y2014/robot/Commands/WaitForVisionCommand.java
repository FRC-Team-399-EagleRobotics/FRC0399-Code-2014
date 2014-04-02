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
public class WaitForVisionCommand extends Command {

    private double timeout = 0.0;
    private boolean found = false;
    boolean timedOut;
    long startTime = 0;

    public WaitForVisionCommand(double timeout) {
        this.timeout = timeout;
    }

    protected void initialize() {
        this.setTimeout(timeout);
        startTime = System.currentTimeMillis();
    }

    protected void execute() {
        
        timedOut = System.currentTimeMillis() - startTime > (long) (timeout * 1000.0);
        if (!timedOut) {
            Robot.getInstance().camera.run();
        }
        found = Robot.getInstance().camera.getHotGoal();
        
        Robot.getInstance().shooter.setOutput(0.0);
    }
    
    protected boolean isFinished() {
        return found || timedOut;
    }
    protected void end(){
        
    }
    protected void interrupted(){
        
    }
}
