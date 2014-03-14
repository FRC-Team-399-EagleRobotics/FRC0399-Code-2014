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
import org.team399.y2014.robot.Commands.IntakeCommand;
import org.team399.y2014.robot.Commands.IntakeDriveCommand;
import org.team399.y2014.robot.Commands.IntakeStageCommand;
import org.team399.y2014.robot.Commands.ShootCommand;
import org.team399.y2014.robot.Commands.StageCommand;
import org.team399.y2014.robot.Commands.StopAllCommand;
import org.team399.y2014.robot.Commands.WaitCommand;
import org.team399.y2014.robot.Config.Constants;

/**
 *
 * @author jeremy.germita@gmail.com (Jeremy Germita)
 */
public class TwoBallAuton extends CommandGroup {

    public TwoBallAuton() {
        System.out.println("TwoBall Auton!");
        this.addSequential(new CalibrateCommand(.500));
        //this.addSequential( new IntakeStageCommand(.5));       
        //this.addSequential(new WaitCommand(.75));
        this.addSequential(new StageCommand(.25));
        // #yolo
        this.addSequential(new IntakeCommand(0, Constants.Intake.EXTENDED, .3));
        this.addSequential(new WaitCommand(.5));
        this.addSequential(new IntakeDriveCommand( .5, -.05, 0.0, Constants.Intake.EXTENDED, 1.0));
//        this.addSequential(new IntakeCommand(-.05, Constants.Intake.EXTENDED, 0));
//        //this.addSequential(new IntakeCommand(0.0, Constants.Intake.EXTENDED,.25));
//        this.addParallel(new ArcadeDriveCommand(.5, 0, 1.0));
//        this.addSequential(new IntakeCommand(0.0, Constants.Intake.EXTENDED,.25));
       // this.addSequential(new IntakeCommand(0.0, Constants.Intake.EXTENDED,1.0));
        //this.addSequential(new IntakeCommand(0.0, Constants.Intake.EXTENDED, .2));
        // this.addSequential(new IntakeCommand(0, Constants.Intake.EXTENDED, 1.0));
        //this.addSequential(new DriveDistanceCommand(-72, .8, 10));
        this.addSequential(new StageCommand(1.5));
        this.addSequential(new ShootCommand(.75));
        //this.addSequential(new WaitCommand(.75));
       // this.addSequential(new StageCommand(.5));
        //this.addSequential(new WaitCommand(.25));
        //this.addSequential(new DriveDistanceCommand(216, 1.0, 10));
        //this.addSequential(new WaitCommand(2.0));
        //this.addParallel(new DriveDistanceCommand(-72, .8, 10));
        this.addSequential(new IntakeStageCommand(.75));
        this.addSequential(new WaitCommand(.25));
        this.addSequential(new IntakeCommand(-1.0, Constants.Intake.EXTENDED, 0.0));
       // this.addSequential(new IntakeCommand(0.0,Constants.Intake.EXTENDED,0.3));
        this.addSequential(new ArcadeDriveCommand(-.4,0,.5));
        this.addSequential(new WaitCommand(.2));
        this.addSequential(new ArcadeDriveCommand(.8,0,.25));
        //this.addSequential(new WaitCommand(.25));
        this.addSequential(new StageCommand(1.5));
        this.addSequential(new ShootCommand(.75));
        this.addSequential(new IntakeStageCommand(.5));
        this.addSequential(new StopAllCommand());
        //this.setTimeout(10);
    }
    
    protected void interrupted() {
    }
}
