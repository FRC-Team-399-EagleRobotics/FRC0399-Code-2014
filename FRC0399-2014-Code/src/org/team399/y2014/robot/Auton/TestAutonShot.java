/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2014.robot.Auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team399.y2014.robot.Commands.CalibrateCommand;
import org.team399.y2014.robot.Commands.IntakeCommand;
import org.team399.y2014.robot.Commands.IntakeStageCommand;
import org.team399.y2014.robot.Commands.ShootCommand;
import org.team399.y2014.robot.Commands.StageCommand;
import org.team399.y2014.robot.Commands.WaitCommand;
import org.team399.y2014.robot.Config.Constants;

/**
 *
 * @author Ivan
 */
public class TestAutonShot extends CommandGroup {

    public TestAutonShot() {
        System.out.println("TwoBall Auton!");
        this.addSequential(new CalibrateCommand(.500));
        this.addSequential(new IntakeCommand(0.0, Constants.Intake.EXTENDED, 0.0));
        this.addSequential(new StageCommand(1.0));
        this.addSequential(new StageCommand(3.0));
        this.addSequential(new ShootCommand(1.0));
        this.addSequential(new WaitCommand(5.0));
        this.addSequential(new StageCommand(1.0));
        this.addSequential(new ShootCommand(1.0));
        this.addSequential(new WaitCommand(5.0));
        this.addSequential(new StageCommand(1.0));
        this.addSequential(new ShootCommand(1.0));
        this.addSequential(new IntakeStageCommand(1.0));
    }
}
