/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Auton;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team399.y2014.Utilities.BooleanWatcher;
import org.team399.y2014.robot.Commands.ArcadeDriveCommand;
import org.team399.y2014.robot.Commands.ConditionalCommand;

/**
 * Test command group for conditional commands.
 *
 * Usage: Construct this command and pass it the argument of a constructed
 * BooleanWatcher object. This object will store the condition for this command
 * while autonomous is running. When this command begins, it checks the
 * condition.
 *
 * All the while the autonomous is running, autonomousPeriodic has some logic to
 * update the BooleanWatcher object with some condition such as hot goal state,
 * drivetrain position, etc.
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class ConditionalCommandTest extends CommandGroup {

    public ConditionalCommandTest(BooleanWatcher condition1) {
        addSequential(new ConditionalCommand(new ArcadeDriveCommand(0.5, 0.0, 5.0),
                new ArcadeDriveCommand(-0.5, 0.0, 5.0),
                condition1));
    }

}
