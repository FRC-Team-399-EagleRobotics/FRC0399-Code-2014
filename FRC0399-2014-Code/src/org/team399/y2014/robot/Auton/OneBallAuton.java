/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team399.y2014.robot.Commands.ArcadeDriveCommand;
import org.team399.y2014.robot.Commands.CalibrateCommand;
import org.team399.y2014.robot.Commands.DriveDistanceCommand;
import org.team399.y2014.robot.Commands.DriveDistanceIntakeCommand;
import org.team399.y2014.robot.Commands.IntakeCommand;
import org.team399.y2014.robot.Commands.IntakeStageCommand;
import org.team399.y2014.robot.Commands.ShootCommand;
import org.team399.y2014.robot.Commands.ShortShotCommand;
import org.team399.y2014.robot.Commands.StageCommand;
import org.team399.y2014.robot.Commands.StopAllCommand;
import org.team399.y2014.robot.Commands.WaitCommand;
import org.team399.y2014.robot.Commands.WaitForVisionCommand;
import org.team399.y2014.robot.Config.Constants;

/**
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class OneBallAuton extends CommandGroup {

    public OneBallAuton() {
        System.out.println("OneBall Auton!");
       // this.addSequential(new CalibrateCommand(.500));
//        //this.addSequential(new WaitCommand(.75));
//      
//        // #yolo
//        this.addSequential( new IntakeStageCommand(.5));// testing to fix comm issues
//        this.addSequential(new IntakeCommand(0, Constants.Intake.EXTENDED, .3));
//        //this.addSequential( new IntakeStageCommand(.5));
//        this.addSequential(new WaitCommand(.5));
//        this.addSequential(new IntakeCommand(-.3, Constants.Intake.EXTENDED, 0));
//        this.addSequential(new ArcadeDriveCommand(.5, 0, 1.2));
//        this.addSequential(new IntakeCommand(0.0, Constants.Intake.EXTENDED,1.0));
//        //this.addSequential(new IntakeCommand(0.0, Constants.Intake.EXTENDED, .2));
//        // this.addSequential(new IntakeCommand(0, Constants.Intake.EXTENDED, 1.0));
//        //this.addSequential(new DriveDistanceCommand(-72, .8, 10));
//        this.addSequential(new StageCommand(1.0));
//        this.addSequential(new WaitCommand(1.25));
//        this.addSequential(new ShootCommand(.75));
//        this.addSequential( new IntakeStageCommand(.5));   Previous One Ball Code
        this.addSequential(new CalibrateCommand(.500));
        this.addSequential(new IntakeStageCommand(.35));

       // this.addSequential(new WaitForVisionCommand(4.0));
        //this.addSequential(new DummyCommand());
        //Drive distance originally -155.0
        this.addSequential(new DriveDistanceIntakeCommand(1.0 ,-125.0 , -.05 , 0.0 , Constants.Intake.EXTENDED , 2.5));
        this.addSequential(new StageCommand(.35));
        //this.addSequential(new IntakeCommand(0.0, Constants.Intake.EXTENDED, 0.0));
        this.addSequential(new ShootCommand(.25)); // originally ShortShotCommand
        this.addSequential(new IntakeStageCommand(.5));
        this.addSequential(new StopAllCommand());
       
    }
}
