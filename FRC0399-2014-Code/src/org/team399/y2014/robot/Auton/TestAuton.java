/* * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team399.y2014.robot.Commands.ArcadeDriveCommand;
import org.team399.y2014.robot.Commands.CalibrateCommand;
import org.team399.y2014.robot.Commands.DriveDistanceCommand;
import org.team399.y2014.robot.Commands.IntakeCommand;
import org.team399.y2014.robot.Commands.IntakeStageCommand;
import org.team399.y2014.robot.Commands.ShootCommand;
import org.team399.y2014.robot.Commands.StageCommand;
import org.team399.y2014.robot.Commands.WaitCommand;
import org.team399.y2014.robot.Config.Constants;

/**
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class TestAuton extends CommandGroup {

    public TestAuton() {
       // this.addSequential(new CalibrateCommand(.500));
        //this.addSequential(new WaitCommand(.75));
       // this.addSequential(new IntakeStageCommand(.75));
        this.addSequential(new DriveDistanceCommand(-180, .5,15.0));
        this.addSequential(new DriveDistanceCommand(96,.65,15.0));
     
    }
}
