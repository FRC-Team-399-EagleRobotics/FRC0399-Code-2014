/*
 * To change this template, choose Tools | Templates
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
import org.team399.y2014.robot.Commands.StageCommand;
import org.team399.y2014.robot.Commands.WaitForVisionCommand;
import org.team399.y2014.robot.Config.Constants;
import org.team399.y2014.robot.Systems.Robot;

/**
 *
 * @author Ivan
 */
public class TwoBallHotGoal extends CommandGroup {
    public TwoBallHotGoal(){
        System.out.println("TwoBall HotGoal Auton!");
        this.addSequential(new CalibrateCommand(.500));
        this.addSequential(new DriveDistanceIntakeCommand(1.0, -96.0 , -.05, 0.0, Constants.Intake.EXTENDED, 1.5));
        this.addSequential(new WaitForVisionCommand(2.0));
        if(Robot.getInstance().camera.getHotGoal() == true){
        this.addSequential(new StageCommand(.5));
        this.addSequential(new ShootCommand(.5));
        this.addSequential(new IntakeStageCommand(.25));
        this.addSequential(new IntakeCommand(-1.0, Constants.Intake.EXTENDED, 1.0));
        this.addSequential(new ArcadeDriveCommand(1.0,45,1.0));
        this.addSequential(new DriveDistanceCommand(1.0,-58.0,1.5));
        this.addSequential(new StageCommand(.5));
        this.addSequential(new ShootCommand(.5));
        this.addSequential(new IntakeStageCommand(.25));
        
        }else{
            this.addSequential(new DriveDistanceCommand(1.0,-58.0,1.5));
            this.addSequential(new StageCommand(.75));
            this.addSequential(new ShootCommand(.5));
            this.addSequential(new IntakeStageCommand(.25));
            this.addSequential(new IntakeCommand(-1.0, Constants.Intake.EXTENDED, 1.0));
            this.addSequential(new StageCommand(2.0));
            this.addSequential(new ShootCommand(.5));
            this.addSequential(new IntakeStageCommand(.25));
            
            
        }
        
    }
    
}
