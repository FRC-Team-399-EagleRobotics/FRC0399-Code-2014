/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 * @author Ivan
 */
public class DummyCommand extends Command {
    
    public DummyCommand() {
    }

    protected void initialize() {
        
    }
    
    protected void execute() {
        System.out.println("dummy command done");
    }
    
    protected void interrupted() {
        
    }
    
    protected void end() {
        
    }

    protected boolean isFinished() {
        return true;
    }
}
