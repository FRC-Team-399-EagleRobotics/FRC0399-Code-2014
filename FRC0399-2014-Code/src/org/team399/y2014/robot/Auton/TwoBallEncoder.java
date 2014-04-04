/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team399.y2014.robot.Commands.ArcadeDriveCommand;
import org.team399.y2014.robot.Commands.CalibrateCommand;
import org.team399.y2014.robot.Commands.DriveDistanceIntakeCommand;
import org.team399.y2014.robot.Commands.IntakeCommand;
import org.team399.y2014.robot.Commands.IntakeTimeDriveCommand;
import org.team399.y2014.robot.Commands.IntakeStageCommand;
import org.team399.y2014.robot.Commands.ShootCommand;
import org.team399.y2014.robot.Commands.ShortShotCommand;
import org.team399.y2014.robot.Commands.StageCommand;
import org.team399.y2014.robot.Commands.StopAllCommand;
import org.team399.y2014.robot.Commands.WaitCommand;
import org.team399.y2014.robot.Config.Constants;
import org.team399.y2014.robot.Config.Constants.Intake;

/**
 *
 * @author Ivan
 */
public class TwoBallEncoder extends CommandGroup {
    
    public TwoBallEncoder(){
        
      System.out.println("TwoBall Encoder Auton!");
        this.addSequential(new CalibrateCommand(.500));
        
       
        // #yolo
        this.addSequential(new IntakeCommand(0, Constants.Intake.EXTENDED, .2));
        this.addSequential(new WaitCommand(.25));
        this.addSequential(new StageCommand(.25));
        this.addSequential(new DriveDistanceIntakeCommand(.85 , -155.0 , -.05 , 0.0 , Constants.Intake.EXTENDED , 2.0 ));
        
        
        //this.addSequential(new StageCommand(.75));
        this.addSequential(new StageCommand(.5));
        this.addSequential(new ShortShotCommand(.5));
        this.addSequential(new WaitCommand(.25));
        this.addSequential(new IntakeCommand(-1,Constants.Intake.EXTENDED,.25));
        this.addSequential(new IntakeTimeDriveCommand(-.2,-1,0.0,Constants.Intake.EXTENDED,.25));
        this.addSequential(new WaitCommand(.2));
        
        this.addSequential(new StageCommand(2.0));
        this.addSequential(new ShortShotCommand(.5));
        this.addSequential(new IntakeStageCommand(.5));
        this.addSequential(new StopAllCommand());
        
    }
    
    protected void interrupted() {
    }
}

    

