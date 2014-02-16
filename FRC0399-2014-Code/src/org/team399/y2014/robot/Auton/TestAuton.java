/* * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team399.y2014.robot.Commands.DriveDistanceCommand;
import org.team399.y2014.robot.Commands.IntakeCommand;
import org.team399.y2014.robot.Config.Constants;

/**
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class TestAuton extends CommandGroup {

    public TestAuton() {
        this.addParallel(new IntakeCommand(-.5, Constants.Intake.EXTENDED, .25));
        this.addSequential(new DriveDistanceCommand(500, .5, 5.0));
        this.addParallel(new IntakeCommand(0, Constants.Intake.RETRACTED, .1));
        
    }

}
