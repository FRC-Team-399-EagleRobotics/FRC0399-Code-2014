/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team399.y2014.robot.Commands.CalibrateCommand;
import org.team399.y2014.robot.Commands.DriveDistanceCommand;
import org.team399.y2014.robot.Commands.IntakeCommand;
import org.team399.y2014.robot.Commands.IntakeStageCommand;
import org.team399.y2014.robot.Config.Constants;

/**
 *
 * @author ivansalazar003@gmail.com (Ivan Salazar)
 */
public class FeederAuton extends CommandGroup {
    
    public FeederAuton(){
        this.addSequential(new CalibrateCommand(.500));
        this.addSequential(new IntakeStageCommand(.4));
        this.addSequential(new IntakeCommand(.5,Constants.Intake.RETRACTED, .5));
        this.addSequential(new IntakeCommand(0.0, Constants.Intake.RETRACTED,1.0));
        this.addSequential(new DriveDistanceCommand(-72,.65,3.0));
    }
    
}
