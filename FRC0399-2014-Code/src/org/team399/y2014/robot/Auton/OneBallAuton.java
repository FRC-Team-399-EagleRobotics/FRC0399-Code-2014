/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team399.y2014.robot.Commands.CalibrateCommand;
import org.team399.y2014.robot.Commands.DriveDistanceCommand;
import org.team399.y2014.robot.Commands.IntakeCommand;
import org.team399.y2014.robot.Commands.ShootCommand;
import org.team399.y2014.robot.Commands.StageCommand;
import org.team399.y2014.robot.Commands.WaitCommand;
import org.team399.y2014.robot.Config.Constants;

/**
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class OneBallAuton extends CommandGroup {

    public OneBallAuton() {
        this.addSequential(new CalibrateCommand(.500));
        //this.addSequential(new WaitCommand(.75));
        this.addSequential(new StageCommand(.25));
        this.addParallel(new WaitCommand(.5));
        this.addParallel(new IntakeCommand(0, Constants.Intake.EXTENDED, 1.0));
        //this.addSequential(new DriveDistanceCommand(-72, .8, 10));
        this.addSequential(new StageCommand(1.0));
        this.addSequential(new WaitCommand(1.5));
        this.addSequential(new ShootCommand(1.0));
        this.addSequential(new WaitCommand(1.0));
        this.addSequential(new StageCommand(.5));
        this.addSequential(new WaitCommand(1.0));
        //this.addSequential(new DriveDistanceCommand(216, 1.0, 10));
        //this.addSequential(new WaitCommand(2.0));
        this.addSequential(new IntakeCommand(0, Constants.Intake.RETRACTED, .1));
        this.addSequential(new StageCommand(.5));
    }
}
