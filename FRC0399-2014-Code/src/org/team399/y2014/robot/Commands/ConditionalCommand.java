/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team399.y2014.Utilities.BooleanWatcher;

/**
 * Class for creating a conditional command.
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class ConditionalCommand extends Command {

    private Command ifTrue, ifFalse, running;    // Objects to store commands
    private BooleanWatcher condition;   // Object for watching a boolean.

    public ConditionalCommand(Command ifTrue,
            Command ifFalse,
            BooleanWatcher condition) {
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
        this.condition = condition;
    }

    protected void initialize() {
        running = condition.get() ? ifTrue : ifFalse;
        running.start();
    }

    protected void execute() {

    }

    protected boolean isFinished() {
        return !running.isRunning() || running.isCanceled();
    }

    protected void interrupted() {
        running.cancel();
    }

    protected void end() {
        running.cancel();
    }

}
