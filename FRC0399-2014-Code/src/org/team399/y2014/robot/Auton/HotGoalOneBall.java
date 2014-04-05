/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team399.y2014.robot.Commands.CalibrateCommand;
import org.team399.y2014.robot.Commands.DriveDistanceCommand;
import org.team399.y2014.robot.Commands.DriveDistanceIntakeCommand;
import org.team399.y2014.robot.Commands.DummyCommand;
import org.team399.y2014.robot.Commands.IntakeCommand;
import org.team399.y2014.robot.Commands.IntakeStageCommand;
import org.team399.y2014.robot.Commands.ShortShotCommand;
import org.team399.y2014.robot.Commands.StageCommand;
import org.team399.y2014.robot.Commands.WaitForVisionCommand;
import org.team399.y2014.robot.Config.Constants;

/**
 *
 * @author Ivan
 */
public class HotGoalOneBall extends CommandGroup {
    
    public HotGoalOneBall(){
        this.addSequential(new CalibrateCommand(.500));
        this.addSequential(new StageCommand(.5));

        this.addSequential(new WaitForVisionCommand(4.0));
        //this.addSequential(new DummyCommand());
        this.addSequential(new DriveDistanceIntakeCommand(1.0 ,-155.0 , -.05 , 0.0 , Constants.Intake.EXTENDED , 2.5));
        this.addSequential(new StageCommand(.35));
        //this.addSequential(new IntakeCommand(0.0, Constants.Intake.EXTENDED, 0.0));
        this.addSequential(new ShortShotCommand(.25));
        this.addSequential(new IntakeStageCommand(.5));
        
    }
    
}
